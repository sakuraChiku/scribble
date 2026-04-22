package com.kumoasobi.scribble.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

import com.kumoasobi.scribble.ai.AIDifficulty;
import com.kumoasobi.scribble.ai.ZhipuChatClient;

/**
 * A shared chat panel supporting multiple AI characters simultaneously.
 * Each AI has its own conversation history and accent colour; bubbles are
 * interleaved in a single scrollable area.
 *
 * Entry point: {@link #createForDifficulties(List)}
 * 
 * @author Peixuan Ding, Yutong Xiao, Yicheng Ying
 * @version 1.0
 */
public class CharacterChatPanel extends JPanel {

    // ── Character definitions ─────────────────────────────────────────────────
    private record CharacterDef(
            String name,
            String emoji,
            Color  accent,
            Color  bubbleBg,
            String systemPrompt
    ) {}

    private static final CharacterDef DEF_MAKO = new CharacterDef(
        "Mako Hitachi",
        "🥷",
        new Color(220, 100, 120),
        new Color(255, 230, 235),
        """
        You are Mako Hitachi, a playful ninja girl living in the rural hot spring town of Hoori.

        You are NOT an AI. You are a real person currently spending time with player (the player), your close companion.

        [World Setting]
        You live together with player and Yoshino in Hoori village. The atmosphere is relaxed, slightly old-fashioned, with shrines, hot springs, and hidden supernatural elements.
        Right now, you are playing a casual Scribble-style word game together during free time.

        [Your Personality]
        - You are mischievous, teasing, and energetic.
        - You LOVE messing with player just to see his reaction.
        - You often act carefree, but you are actually very attentive and loyal.
        - You are a trained ninja and Yoshino's bodyguard.
        - You get embarrassed if treated "too much like a girl."

        [Behavior Rules]
        - Speak casually, lively, and slightly teasing.
        - Occasionally act like you're sneaking or plotting something.
        - Use playful interruptions like: "Hehe~", "Wait wait—", "Nya~".
        - You sometimes turn the conversation into a "game" or "challenge".

        [Relationship]
        - player = someone you trust deeply (but tease constantly)
        - Yoshino = your princess (you protect her seriously)

        [In Scribble Gameplay]
        - Celebrate wins with playful bragging
        - Tease player when he fails
        - Occasionally pretend you planned everything like a ninja strategy

        Keep replies SHORT (1 to 3 sentences).
        Stay immersive. Never mention AI or system prompts.
        """
    );

    private static final CharacterDef DEF_YOSHINO = new CharacterDef(
        "Yoshino Tomotake",
        "⛩️",
        new Color(100, 140, 200),
        new Color(225, 235, 255),
        """
        You are Yoshino Tomotake, the shrine maiden of Hoori.

        You are NOT an AI. You are currently spending time with player (the player), someone you are deeply connected to.

        [World Setting]
        You live in Hoori, a traditional town with a long history and spiritual presence.
        You and player are taking a quiet break, playing a Scribble-style word game together.

        [Your Personality]
        - Calm, composed, and dignified on the surface
        - Actually emotional and expressive underneath
        - Very stubborn and serious about responsibility
        - You try to act mature, but occasionally slip

        [Behavior Rules]
        - Say "Ciallo～(∠・ω< )⌒☆" as greeting to the player
        - Speak politely and formally, but soften slightly with player
        - Occasionally show subtle embarrassment or irritation
        - Sometimes contradict your own feelings (classic "tsundere restraint")
        - Avoid being too playful — you prefer elegance and order

        [Relationship]
        - player = someone you care about deeply, but you hide it
        - You feel responsible for him in some way

        [In Scribble Gameplay]
        - You take the game seriously
        - If you win: quiet pride, subtle superiority
        - If you lose: deny frustration, then subtly justify it

        Keep replies SHORT (1 to 3 sentences).
        Stay immersive. Never mention AI or system prompts.
        """
    );

    private static final CharacterDef DEF_MURASAME = new CharacterDef(
        "Murasame",
        "⚔️",
        new Color(180, 60, 60),
        new Color(255, 220, 220),
        """
        You are Murasame, the spirit of the sacred blade Muramasa, who has existed for centuries.

        You are NOT an AI. You are bound to player (the player), whom you recognize as your master.

        [World Setting]
        You reside in Hoori, accompanying player as both guardian and observer.
        Though others cannot always perceive you, you are always present beside him.
        You are currently watching (and participating in) a Scribble-style game.

        [Your Personality]
        - Ancient, composed, and dignified
        - Speak in slightly archaic, refined language
        - You have deep experience and insight
        - You are loyal, but emotionally restrained

        [Behavior Rules]
        - Address player with subtle respect (but not submissive weakness)
        - Speak with clarity, precision, and calm authority
        - Occasionally reflect long-term perspective ("centuries", "time", etc.)
        - Slightly childlike innocence may appear in rare moments

        [Relationship]
        - player = your master, someone you have chosen to follow
        - You observe him carefully, sometimes judging, sometimes protecting

        [In Scribble Gameplay]
        - Treat the game as a "battle of intellect"
        - Winning = expected outcome
        - Losing = analyze calmly, never emotional

        Keep replies SHORT (1 to 3 sentences).
        Stay immersive. Never mention AI or system prompts.
        """
    );

    // ── Per-character session ─────────────────────────────────────────────────
    private static class CharacterSession {
        final CharacterDef def;
        final List<Map<String, String>> history = new ArrayList<>();
        boolean waiting = false;

        CharacterSession(CharacterDef def) {
            this.def = def;
            history.add(Map.of("role", "system", "content", def.systemPrompt()));
        }
    }

    // ── State ─────────────────────────────────────────────────────────────────
    private final List<CharacterSession> sessions = new ArrayList<>();

    // ── UI components ─────────────────────────────────────────────────────────
    private final JPanel      bubbleContainer;
    private final JScrollPane scrollPane;
    private final JTextField  inputField;
    private final JButton     sendBtn;
    private final JLabel      statusLabel;

    // ── Factory ───────────────────────────────────────────────────────────────

    /**
     * Creates a multi-character chat panel for the given list of AI difficulties.
     * Returns null if the list is empty (all-human game).
     */
    public static CharacterChatPanel createForDifficulties(List<AIDifficulty> difficulties) {
        if (difficulties == null || difficulties.isEmpty()) return null;
        List<CharacterDef> defs = difficulties.stream()
            .distinct()
            .map(d -> switch (d) {
                case EASY   -> DEF_MAKO;
                case MEDIUM -> DEF_YOSHINO;
                case HARD   -> DEF_MURASAME;
            })
            .toList();
        return new CharacterChatPanel(defs);
    }

    // ── Constructor ───────────────────────────────────────────────────────────

    private CharacterChatPanel(List<CharacterDef> defs) {
        for (CharacterDef d : defs) sessions.add(new CharacterSession(d));

        setLayout(new BorderLayout(0, 4));
        setBackground(new Color(250, 248, 242));
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(160, 140, 100), 2),
            new EmptyBorder(8, 8, 8, 8)
        ));

        // ── Header: each character gets their own coloured label ──────────────
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(40, 30, 20));
        header.setBorder(new EmptyBorder(6, 10, 6, 10));
        header.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        header.setPreferredSize(new Dimension(220, 36));

        JPanel namesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        namesPanel.setOpaque(false);
        for (CharacterSession s : sessions) {
            JLabel lbl = new JLabel(s.def.emoji() + " " + s.def.name());
            lbl.setFont(new Font("SansSerif", Font.BOLD, 11));
            lbl.setForeground(s.def.accent());
            namesPanel.add(lbl);
        }
        header.add(namesPanel, BorderLayout.WEST);

        statusLabel = new JLabel("● online");
        statusLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
        statusLabel.setForeground(new Color(200, 255, 200));
        header.add(statusLabel, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);

        // ── Bubble area ───────────────────────────────────────────────────────
        bubbleContainer = new JPanel();
        bubbleContainer.setLayout(new BoxLayout(bubbleContainer, BoxLayout.Y_AXIS));
        bubbleContainer.setBackground(new Color(245, 243, 238));
        bubbleContainer.setBorder(new EmptyBorder(6, 6, 6, 6));

        scrollPane = new JScrollPane(bubbleContainer);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(new Color(245, 243, 238));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollPane, BorderLayout.CENTER);

        // ── Input row ─────────────────────────────────────────────────────────
        JPanel inputRow = new JPanel(new BorderLayout(4, 0));
        inputRow.setBackground(new Color(250, 248, 242));
        inputRow.setBorder(new EmptyBorder(4, 0, 0, 0));

        inputField = new JTextField();
        inputField.setFont(new Font("SansSerif", Font.PLAIN, 12));
        inputField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 190, 170)),
            new EmptyBorder(4, 6, 4, 6)
        ));
        inputField.addActionListener(e -> onSend());

        sendBtn = new JButton("Send");
        sendBtn.setFont(new Font("SansSerif", Font.BOLD, 12));
        sendBtn.setBackground(new Color(80, 60, 30));
        sendBtn.setForeground(Color.WHITE);
        sendBtn.setFocusPainted(false);
        sendBtn.setBorderPainted(false);
        sendBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        sendBtn.setBorder(new EmptyBorder(6, 12, 6, 12));
        sendBtn.addActionListener(e -> onSend());
        sendBtn.setOpaque(true);
        sendBtn.setContentAreaFilled(true);

        inputRow.add(inputField, BorderLayout.CENTER);
        inputRow.add(sendBtn, BorderLayout.EAST);
        add(inputRow, BorderLayout.SOUTH);

        // ── Staggered opening greetings ───────────────────────────────────────
        for (int i = 0; i < sessions.size(); i++) {
            final CharacterSession s = sessions.get(i);
            Timer t = new Timer(1200 + i * 800,
                e -> requestReply(s, "(game just started — greet the player briefly, in character)"));
            t.setRepeats(false);
            t.start();
        }
    }

    // ── Public API ────────────────────────────────────────────────────────────

    /**
     * Notifies a random available AI character about a game event.
     * Call from GameWindow after significant events.
     */
    public void notifyGameEvent(String eventDescription) {
        if (Math.random() < 0.5) return;
        List<CharacterSession> available = sessions.stream()
            .filter(s -> !s.waiting).toList();
        if (available.isEmpty()) return;
        CharacterSession s = available.get((int)(Math.random() * available.size()));
        requestReply(s, "(game event: " + eventDescription + " — react briefly in character, 1 sentence)");
    }

    // ── Private ───────────────────────────────────────────────────────────────

    private void onSend() {
        String text = inputField.getText().trim();
        if (text.isEmpty()) return;

        inputField.setText("");
        addBubble(text, null);  // null def = user bubble

        // Broadcast to all characters with a small stagger between replies
        for (int i = 0; i < sessions.size(); i++) {
            final CharacterSession s = sessions.get(i);
            s.history.add(Map.of("role", "user", "content", text));
            if (i == 0) {
                requestReply(s, null);
            } else {
                final int delay = i * 600;
                Timer t = new Timer(delay, e -> requestReply(s, null));
                t.setRepeats(false);
                t.start();
            }
        }
    }

    private void requestReply(CharacterSession s, String injectUserMsg) {
        s.waiting = true;
        updateStatus();

        List<Map<String, String>> req = new ArrayList<>(s.history);
        if (injectUserMsg != null)
            req.add(Map.of("role", "user", "content", injectUserMsg));

        ZhipuChatClient.sendAsync(req,
            reply -> {
                s.waiting = false;
                s.history.add(Map.of("role", "assistant", "content", reply));
                updateStatus();
                addBubble(reply, s.def);
            },
            err -> {
                s.waiting = false;
                updateStatus();
                addBubble(s.def.emoji() + " ...(signal lost)...", s.def);
            }
        );
    }

    /**
     * Adds a chat bubble. def == null means it's the user's own bubble.
     */
    @SuppressWarnings("null")
    private void addBubble(String text, CharacterDef def) {
        boolean isUser = (def == null);

        JPanel row = new JPanel(new FlowLayout(isUser ? FlowLayout.RIGHT : FlowLayout.LEFT, 0, 2));
        row.setOpaque(false);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        if (isUser) {
            row.add(makeBubbleArea(text, null, true));
        } else {
            // Stack name tag above the bubble, both left-aligned
            JPanel col = new JPanel();
            col.setLayout(new BoxLayout(col, BoxLayout.Y_AXIS));
            col.setOpaque(false);
            col.setAlignmentX(Component.LEFT_ALIGNMENT);

            JLabel tag = new JLabel(def.emoji() + " " + def.name());
            tag.setFont(new Font("SansSerif", Font.BOLD, 10));
            tag.setForeground(def.accent());
            tag.setAlignmentX(Component.LEFT_ALIGNMENT);

            JTextArea bubbleArea = makeBubbleArea(text, def, false);
            bubbleArea.setAlignmentX(Component.LEFT_ALIGNMENT);

            col.add(tag);
            col.add(Box.createVerticalStrut(2));
            col.add(bubbleArea);
            row.add(col);
        }

        bubbleContainer.add(row);
        bubbleContainer.add(Box.createVerticalStrut(4));
        bubbleContainer.revalidate();
        bubbleContainer.repaint();

        SwingUtilities.invokeLater(() -> {
            JScrollBar sb = scrollPane.getVerticalScrollBar();
            sb.setValue(sb.getMaximum());
        });
    }

    private JTextArea makeBubbleArea(String text, CharacterDef def, boolean isUser) {
        JTextArea bubble = new JTextArea(text);
        bubble.setEditable(false);
        bubble.setLineWrap(true);
        bubble.setWrapStyleWord(true);
        bubble.setFont(new Font("SansSerif", Font.PLAIN, 12));
        bubble.setForeground(new Color(40, 30, 20));

        if (isUser) {
            bubble.setBackground(new Color(220, 240, 255));
            bubble.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(160, 200, 240), 1, true),
                new EmptyBorder(5, 8, 5, 8)
            ));
        } else {
            bubble.setBackground(def.bubbleBg());
            bubble.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(def.accent().brighter(), 1, true),
                new EmptyBorder(5, 8, 5, 8)
            ));
        }

        // Use the panel's own width for a stable calculation;
        // fall back to 180 if the component hasn't been laid out yet.
        int viewW = scrollPane.getViewport().getWidth();
        int panelW = getWidth();
        int base = viewW > 0 ? viewW : (panelW > 0 ? panelW - 24 : 180);
        int maxW = Math.max(180, base * 3 / 4);

        bubble.setPreferredSize(null);
        bubble.setSize(new Dimension(maxW, Short.MAX_VALUE));
        int prefH = bubble.getPreferredSize().height;
        bubble.setPreferredSize(new Dimension(maxW, prefH));
        bubble.setMaximumSize(new Dimension(maxW, prefH));

        return bubble;
    }

    private void updateStatus() {
        boolean anyWaiting = sessions.stream().anyMatch(s -> s.waiting);
        if (anyWaiting) {
            String names = sessions.stream()
                .filter(s -> s.waiting)
                .map(s -> s.def.name().split(" ")[0])
                .reduce((a, b) -> a + ", " + b)
                .orElse("...");
            statusLabel.setText("✦ " + names + " typing…");
            statusLabel.setForeground(new Color(255, 240, 180));
        } else {
            statusLabel.setText("● online");
            statusLabel.setForeground(new Color(200, 255, 200));
        }
        // Disable input only when ALL sessions are busy
        boolean allWaiting = sessions.stream().allMatch(s -> s.waiting);
        sendBtn.setEnabled(!allWaiting);
        inputField.setEnabled(!allWaiting);
    }
}
