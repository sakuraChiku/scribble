package com.kumoasobi.scribble.ai;

import java.util.*;

import com.kumoasobi.scribble.models.*;
import com.kumoasobi.scribble.rules.scanner.WordScanner;
import com.kumoasobi.scribble.rules.validator.*;

/**
 * Scrabble AI engine.
 *
 * Algorithm overview:
 * ─────────────────────────────────────────────────────────────────────────────
 * 1. Find all "anchor" cells — empty cells adjacent to an existing tile
 *    (or the centre square on the first move).
 * 2. For each anchor, try placing every permutation of 1–7 rack tiles
 *    horizontally and vertically, starting at the anchor.
 * 3. Use the existing validators (BoardValidator + DictValidator) to filter
 *    out illegal moves, and WordScanner to compute scores.
 * 4. Select from the valid candidate list according to difficulty:
 *      EASY   → random pick
 *      MEDIUM → best score among a 40-anchor sample
 *      HARD   → best score across the full board
 *
 * This greedy approach is straightforward to implement with the existing
 * game infrastructure and runs fast enough for real-time play.
 * ─────────────────────────────────────────────────────────────────────────────
 */
public class ScrabbleAI {

    private final AIDifficulty difficulty;
    private final Set<String>  dictionary;
    private final Random       rng = new Random();

    // How many anchors MEDIUM difficulty samples (out of all available)
    private static final int MEDIUM_ANCHOR_SAMPLE = 40;

    public ScrabbleAI(AIDifficulty difficulty, Set<String> dictionary) {
        this.difficulty = difficulty;
        this.dictionary = dictionary;
    }

    // ── Public API ────────────────────────────────────────────────────────────

    /**
     * Find the best move for the given player on the given board.
     * Returns null if no valid move exists (caller should skip the turn).
     */
    public AIMove findBestMove(Board board, Player player) {
        List<int[]> anchors = getAnchors(board);
        if (anchors.isEmpty()) return null;

        // MEDIUM: sample a random subset of anchors for speed
        if (difficulty == AIDifficulty.MEDIUM && anchors.size() > MEDIUM_ANCHOR_SAMPLE) {
            Collections.shuffle(anchors, rng);
            anchors = anchors.subList(0, MEDIUM_ANCHOR_SAMPLE);
        }

        List<AIMove> candidates = new ArrayList<>();
        List<Tile> rack = new ArrayList<>(player.getRack());

        for (int[] anchor : anchors) {
            int row = anchor[0], col = anchor[1];
            // Try both directions from each anchor
            collectMoves(candidates, board, rack, row, col, Direction.HORIZONTAL);
            collectMoves(candidates, board, rack, row, col, Direction.VERTICAL);
        }

        if (candidates.isEmpty()) return null;

        return switch (difficulty) {
            case EASY   -> candidates.get(rng.nextInt(candidates.size()));
            case MEDIUM, HARD -> candidates.stream()
                .max(Comparator.comparingInt(AIMove::getScore))
                .orElse(null);
        };
    }

    // ── Anchor detection ──────────────────────────────────────────────────────

    /**
     * Returns all empty cells that are adjacent to a placed tile.
     * On a completely empty board, returns only the centre cell (8, 8).
     */
    private List<int[]> getAnchors(Board board) {
        List<int[]> anchors = new ArrayList<>();
        boolean boardEmpty = true;
        int size = Board.getSIZE();

        for (int r = 1; r <= size; r++) {
            for (int c = 1; c <= size; c++) {
                if (board.hasTile(r, c)) {
                    boardEmpty = false;
                } else if (board.hasAdjacentTile(r, c)) {
                    anchors.add(new int[]{r, c});
                }
            }
        }

        if (boardEmpty) {
            anchors.add(new int[]{8, 8}); // centre
        }
        return anchors;
    }

    // ── Move generation ───────────────────────────────────────────────────────

    /**
     * Try placing 1–(rack.size()) tiles starting at (row, col) in the given
     * direction, and collect all valid moves into `candidates`.
     */
    private void collectMoves(List<AIMove> candidates, Board board,
                              List<Tile> rack, int row, int col, Direction dir) {
        // Generate all subsets (permutations) of rack tiles of length 1..7
        List<List<Tile>> perms = new ArrayList<>();
        generatePermutations(rack, new ArrayList<>(), new boolean[rack.size()], perms, 1);

        int size = Board.getSIZE();

        for (List<Tile> perm : perms) {
            // Try placing this sequence starting at (row, col) in direction dir
            // and also at offsets that push the anchor inside the word
            for (int offset = 0; offset < perm.size(); offset++) {
                Move move = buildMove(board, perm, row, col, dir, offset, size);
                if (move == null) continue;

                // Validate using existing infrastructure
                MoveResult result = quickValidate(board, move);
                if (result != null && result.isValidMove()) {
                    candidates.add(new AIMove(move, result.getTotalScore()));
                }
            }
        }
    }

    /**
     * Attempt to lay `tiles` starting from (startRow, startCol) offset by
     * `anchorOffset` positions backwards in `dir`, skipping over cells that
     * already have tiles on the board.
     *
     * Returns null if the word would go out of bounds.
     */
    private Move buildMove(Board board, List<Tile> tiles,
                           int anchorRow, int anchorCol,
                           Direction dir, int anchorOffset, int size) {
        // Compute the actual start position
        int startRow = anchorRow - anchorOffset * dir.getDy();
        int startCol = anchorCol - anchorOffset * dir.getDx();

        Move move = new Move();
        int r = startRow, c = startCol;
        int tileIdx = 0;

        while (tileIdx < tiles.size()) {
            if (r < 1 || r > size || c < 1 || c > size) return null; // out of bounds

            if (board.hasTile(r, c)) {
                // Skip over already-placed tiles
                r += dir.getDy();
                c += dir.getDx();
                continue;
            }

            try {
                move.addPlacement(new Placement(tiles.get(tileIdx), r, c));
            } catch (IllegalArgumentException e) {
                return null;
            }
            tileIdx++;
            r += dir.getDy();
            c += dir.getDx();
        }

        if (move.isEmpty()) return null;

        // Set direction
        if (dir == Direction.HORIZONTAL) move.sameRow();
        else move.sameCol();

        // Handle single-tile: infer direction from context
        if (move.getPlacements().size() == 1 && move.getDirection() == null) {
            Placement p = move.getPlacements().get(0);
            boolean hv = board.hasTile(p.getRow()-1, p.getCol()) || board.hasTile(p.getRow()+1, p.getCol());
            boolean hh = board.hasTile(p.getRow(), p.getCol()-1) || board.hasTile(p.getRow(), p.getCol()+1);
            if (hv && !hh) move.sameCol(); else move.sameRow();
        }

        return move;
    }

    // ── Validation (inline, no exception overhead) ────────────────────────────

    /**
     * Run the full validation pipeline (board + dict) and return a MoveResult,
     * or null if a system error occurs.
     * Temporarily places then recalls tiles for scanning.
     */
    private MoveResult quickValidate(Board board, Move move) {
        try {
            // Board structure check (no player-tile check — AI always has tiles)
            BoardValidator.validateBoard(move, board);
        } catch (Exception e) {
            return null;
        }

        // Temporarily place to scan
        board.placeMove(move);
        List<WordInfo> wordInfos = new ArrayList<>();
        try {
            Direction dir = move.getDirection();
            WordInfo main = WordScanner.scanWord(board, move.getPlacements().get(0), dir);
            wordInfos.add(main);
            for (Placement p : move.getPlacements()) {
                WordInfo cross = WordScanner.scanWord(board, p, dir.flip());
                if (cross.getWord().length() > 1) wordInfos.add(cross);
            }
        } catch (Exception e) {
            board.recallMove(move);
            return null;
        }
        board.recallMove(move);

        // Dictionary check
        if (dictionary != null && !dictionary.isEmpty()) {
            for (WordInfo wi : wordInfos) {
                try {
                    DictValidator.isValidWord(wi, dictionary);
                } catch (Exception e) {
                    return null;
                }
            }
        }

        int total = wordInfos.stream().mapToInt(WordInfo::getScore).sum();
        List<String> words = wordInfos.stream().map(WordInfo::getWord).toList();
        return new MoveResult(true, total, words, "AI move");
    }

    // ── Permutation generator ─────────────────────────────────────────────────

    /**
     * Generate all permutations of rack tiles with length >= minLen.
     * Deduplicates by tile letter+score to avoid redundant search.
     */
    private void generatePermutations(List<Tile> rack, List<Tile> current,
                                      boolean[] used, List<List<Tile>> result,
                                      int minLen) {
        if (current.size() >= minLen) {
            result.add(new ArrayList<>(current));
        }
        if (current.size() == rack.size() || current.size() == 7) return;

        Set<String> seen = new HashSet<>();
        for (int i = 0; i < rack.size(); i++) {
            if (used[i]) continue;
            Tile t = rack.get(i);
            // Deduplicate identical tiles (same letter + score)
            String key = t.getLetter() + ":" + t.getScore();
            if (seen.contains(key)) continue;
            seen.add(key);

            used[i] = true;
            current.add(t);
            generatePermutations(rack, current, used, result, minLen);
            current.remove(current.size() - 1);
            used[i] = false;
        }
    }
}
