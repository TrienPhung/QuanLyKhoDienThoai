package View.Panel;

import View.Component.IntegratedSearch;
import View.Component.MainFunction;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import View.Component.PanelBorderRadius;
import View.Component.itemTaskbar;
import View.Dialog.ThuocTinhSanPham.DungLuongRamDialog;
import View.Dialog.ThuocTinhSanPham.DungLuongRomDialog;
import View.Dialog.ThuocTinhSanPham.HeDieuHanhDialog;
import View.Dialog.ThuocTinhSanPham.MauSacDialog;
import View.Dialog.ThuocTinhSanPham.ThuongHieuDialog;
import View.Dialog.ThuocTinhSanPham.XuatXuDialog;
import View.Main;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.Normalizer;

public class QuanLyThuocTinhSP extends JPanel {

    // Modern Colors
    private static final Color BACKGROUND_PRIMARY = new Color(102, 126, 234);
    private static final Color BACKGROUND_SECONDARY = new Color(118, 75, 162);
    private static final Color BACKGROUND_LIGHT = new Color(250, 250, 250);
    private static final Color CARD_BACKGROUND = Color.WHITE;
    private static final Color TEXT_PRIMARY = new Color(30, 41, 59);
    private static final Color TEXT_SECONDARY = new Color(100, 116, 139);
    private static final Color ACCENT_COLOR = new Color(102, 126, 234);
    private static final Color HOVER_COLOR = new Color(248, 250, 252);

    // Components
    private JPanel headerPanel, contentPanel, statsPanel, searchPanel, gridPanel;
    private JTextField searchField;
    private ModernCard[] cards;
    private JLabel titleLabel, subtitleLabel;
    private Timer animationTimer;
    private Main m;

    // Data - Thay đổi từ emoji sang SVG icons
    private String[] icons = {"brand_100px.svg", "factory_100px.svg", "os_100px.svg", "ram_100px.svg", "rom_100px.svg", "color_100px.svg"};
    private String[] titles = {"Thương hiệu", "Xuất xứ", "Hệ điều hành", "RAM", "ROM", "Màu sắc"};
    private String[] descriptions = {
        "Quản lý danh sách các thương hiệu sản phẩm công nghệ hàng đầu",
        "Theo dõi nguồn gốc sản phẩm từ các quốc gia và khu vực",
        "Danh sách hệ điều hành và phiên bản tương thích",
        "Các mức dung lượng RAM từ cơ bản đến cao cấp",
        "Bộ nhớ trong và tùy chọn mở rộng cho sản phẩm",
        "Bảng màu phong phú với các tùy chọn hiện đại"
    };
    private int[] counts = {0, 0, 0, 0, 0, 0}; // Sẽ được cập nhật từ database

    public QuanLyThuocTinhSP(Main m) {
        this.m = m;
        loadDataCounts(); // Load số lượng từ database
        initComponents();
        setupLayout();
        addEventListeners();
        startAnimations();
    }

    // Method để load số lượng từ database
    private void loadDataCounts() {
        try {
            // Giả sử bạn có các DAO để truy xuất dữ liệu
            // counts[0] = thuongHieuDAO.getCount();
            // counts[1] = xuatXuDAO.getCount();
            // counts[2] = heDieuHanhDAO.getCount();
            // counts[3] = ramDAO.getCount();
            // counts[4] = romDAO.getCount();
            // counts[5] = mauSacDAO.getCount();

            // Tạm thời sử dụng giá trị mặc định, bạn có thể thay thế bằng code thực tế
            counts[0] = getDatabaseCount("thuong_hieu");
            counts[1] = getDatabaseCount("xuat_xu");
            counts[2] = getDatabaseCount("he_dieu_hanh");
            counts[3] = getDatabaseCount("ram");
            counts[4] = getDatabaseCount("rom");
            counts[5] = getDatabaseCount("mau_sac");
        } catch (Exception e) {
            // Nếu có lỗi, sử dụng giá trị mặc định
            counts = new int[]{15, 12, 8, 6, 10, 20};
        }
    }

    // Method để lấy số lượng từ database (bạn cần implement theo hệ thống của mình)
    private int getDatabaseCount(String tableName) {
        // Đây là ví dụ, bạn cần thay thế bằng logic truy xuất database thực tế
        // Connection conn = DatabaseConnection.getConnection();
        // PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) FROM " + tableName);
        // ResultSet rs = stmt.executeQuery();
        // return rs.next() ? rs.getInt(1) : 0;

        // Tạm thời return giá trị giả lập
        switch (tableName) {
            case "thuong_hieu":
                return 15;
            case "xuat_xu":
                return 12;
            case "he_dieu_hanh":
                return 8;
            case "ram":
                return 6;
            case "rom":
                return 10;
            case "mau_sac":
                return 20;
            default:
                return 0;
        }
    }

    // Method để refresh dữ liệu
    public void refreshData() {
        loadDataCounts();
        updateCards();
        updateStats();
    }

    private void updateCards() {
        if (cards != null) {
            for (int i = 0; i < cards.length; i++) {
                cards[i].updateCount(counts[i]);
            }
        }
    }

    private void updateStats() {
        if (statsPanel != null) {
            Component[] components = statsPanel.getComponents();
            if (components.length > 1 && components[1] instanceof StatCard) {
                StatCard totalCard = (StatCard) components[1];
                int total = 0;
                for (int count : counts) {
                    total += count;
                }
                totalCard.updateValue(String.valueOf(total));
            }
        }
    }

    private void initComponents() {
        setBackground(BACKGROUND_LIGHT);
        setLayout(new BorderLayout());

        // Header Panel - Giảm chiều cao từ 180 xuống 120
        headerPanel = new GradientPanel();
        headerPanel.setPreferredSize(new Dimension(0, 120));
        headerPanel.setLayout(new BorderLayout());

        JPanel headerContent = new JPanel(new GridBagLayout());
        headerContent.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();

        // Giảm font size của title từ 36 xuống 28
        titleLabel = new JLabel("🚀 Quản lý Thuộc tính");
        titleLabel.setFont(new Font("Inter", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Giảm font size của subtitle từ 16 xuống 14
        subtitleLabel = new JLabel("Hệ thống quản lý hiện đại cho sản phẩm công nghệ");
        subtitleLabel.setFont(new Font("Inter", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(255, 255, 255, 200));
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 8, 0);
        headerContent.add(titleLabel, gbc);
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 0, 0);
        headerContent.add(subtitleLabel, gbc);

        headerPanel.add(headerContent, BorderLayout.CENTER);

        // Content Panel
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(BACKGROUND_LIGHT);
        contentPanel.setBorder(new EmptyBorder(20, 40, 30, 40)); // Giảm padding top từ 30 xuống 20

        // Stats Panel
        setupStatsPanel();

        // Search Panel
        setupSearchPanel();

        // Grid Panel
        setupGridPanel();

        // Add components to content
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(BACKGROUND_LIGHT);
        topPanel.add(statsPanel, BorderLayout.NORTH);
        topPanel.add(searchPanel, BorderLayout.CENTER);

        contentPanel.add(topPanel, BorderLayout.NORTH);
        contentPanel.add(gridPanel, BorderLayout.CENTER);

        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
    }

    private void setupStatsPanel() {
        statsPanel = new JPanel(new GridLayout(1, 4, 20, 0));
        statsPanel.setBackground(BACKGROUND_LIGHT);
        statsPanel.setBorder(new EmptyBorder(0, 0, 25, 0)); // Giảm padding từ 30 xuống 25

        String[] statLabels = {"Nhóm thuộc tính", "Tổng mục con", "Độ chính xác", "Hoạt động"};
        String[] statValues = {"6", calculateTotalItems(), "99%", "24/7"};

        for (int i = 0; i < 4; i++) {
            JPanel statCard = new StatCard(statValues[i], statLabels[i]);
            statsPanel.add(statCard);
        }
    }

    private String calculateTotalItems() {
        int total = 0;
        for (int count : counts) {
            total += count;
        }
        return String.valueOf(total);
    }

    private void setupSearchPanel() {
        searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        searchPanel.setBackground(BACKGROUND_LIGHT);
        searchPanel.setBorder(new EmptyBorder(0, 0, 25, 0));

        JPanel searchContainer = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(Color.WHITE);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                g2d.setColor(new Color(226, 232, 240));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 25, 25);
            }
        };
        searchContainer.setLayout(new BorderLayout());
        searchContainer.setPreferredSize(new Dimension(400, 50));
        searchContainer.setOpaque(false);
        searchContainer.setBorder(new EmptyBorder(0, 20, 0, 20));

        JLabel searchIcon = new JLabel("🔍");
        searchIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        searchIcon.setBorder(new EmptyBorder(0, 0, 0, 10));

        searchField = new JTextField();
        searchField.setFont(new Font("Inter", Font.PLAIN, 14));
        searchField.setBorder(null);
        searchField.setOpaque(false);
        searchField.setForeground(TEXT_SECONDARY);
        searchField.setText("Tìm kiếm thuộc tính nhanh...");

        searchContainer.add(searchIcon, BorderLayout.WEST);
        searchContainer.add(searchField, BorderLayout.CENTER);

        searchPanel.add(searchContainer);
    }

    private void setupGridPanel() {
        gridPanel = new JPanel(new GridLayout(2, 3, 25, 25));
        gridPanel.setBackground(BACKGROUND_LIGHT);

        cards = new ModernCard[6];
        for (int i = 0; i < 6; i++) {
            cards[i] = new ModernCard(icons[i], titles[i], descriptions[i], counts[i], i);
            gridPanel.add(cards[i]);
        }
    }

    private void addEventListeners() {
        // Search functionality
        searchField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (searchField.getText().equals("Tìm kiếm thuộc tính nhanh...")) {
                    searchField.setText("");
                    searchField.setForeground(TEXT_PRIMARY);
                }
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                if (searchField.getText().isEmpty()) {
                    searchField.setText("Tìm kiếm thuộc tính nhanh...");
                    searchField.setForeground(TEXT_SECONDARY);
                }
            }
        });

        searchField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                filterCards(searchField.getText().toLowerCase());
            }
        });
    }

    public static String normalizeString(String input) {
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        return normalized.replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                .toLowerCase()
                .trim();
    }

    private void filterCards(String searchTerm) {
        String normalizedSearch = normalizeString(searchTerm);

        for (ModernCard card : cards) {
            String normalizedTitle = normalizeString(card.getTitle());
            boolean visible = searchTerm.isEmpty()
                    || searchTerm.equals("tìm kiếm thuộc tính nhanh...")
                    || normalizedTitle.contains(normalizedSearch);
            card.setVisible(visible);
        }
        gridPanel.revalidate();
        gridPanel.repaint();
    }

    private void startAnimations() {
        // Đảm bảo tất cả cards ban đầu đều ẩn
        for (ModernCard card : cards) {
            card.setInitialAlpha(0.0f);
        }
        
        // Fade in animation for cards với delay giữa các card
        Timer fadeInTimer = new Timer(150, new ActionListener() { // Tăng delay từ 100ms lên 150ms
            int cardIndex = 0;

            public void actionPerformed(ActionEvent e) {
                if (cardIndex < cards.length) {
                    cards[cardIndex].startFadeIn();
                    cardIndex++;
                } else {
                    ((Timer) e.getSource()).stop();
                }
            }
        });
        
        // Thêm delay ban đầu để đảm bảo UI đã render xong
        fadeInTimer.setInitialDelay(300);
        fadeInTimer.start();
    }

    private void setupLayout() {
        setBorder(new EmptyBorder(0, 0, 0, 0));
    }

    // Custom Components
    private class GradientPanel extends JPanel {

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            GradientPaint gradient = new GradientPaint(
                    0, 0, BACKGROUND_PRIMARY,
                    getWidth(), getHeight(), BACKGROUND_SECONDARY
            );
            g2d.setPaint(gradient);
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    private class StatCard extends JPanel {

        private String value, label;
        private float alpha = 0.0f;

        public StatCard(String value, String label) {
            this.value = value;
            this.label = label;
            setBackground(CARD_BACKGROUND);
            setPreferredSize(new Dimension(200, 100)); // Giảm chiều cao từ 100 xuống 80

            Timer fadeIn = new Timer(20, new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    alpha = Math.min(1.0f, alpha + 0.05f);
                    repaint();
                    if (alpha >= 1.0f) {
                        ((Timer) e.getSource()).stop();
                    }
                }
            });
            fadeIn.setInitialDelay(500);
            fadeIn.start();
        }

        public void updateValue(String newValue) {
            this.value = newValue;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));

            // Draw rounded background
            g2d.setColor(CARD_BACKGROUND);
            g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

            // Draw shadow
            g2d.setColor(new Color(0, 0, 0, 20));
            g2d.fillRoundRect(2, 2, getWidth() - 2, getHeight() - 2, 20, 20);

            // Draw content - điều chỉnh vị trí cho phù hợp với chiều cao mới
            g2d.setColor(ACCENT_COLOR);
            g2d.setFont(new Font("Inter", Font.BOLD, 28)); // Giảm font size từ 32 xuống 28
            FontMetrics fm = g2d.getFontMetrics();
            int valueWidth = fm.stringWidth(value);
            g2d.drawString(value, (getWidth() - valueWidth) / 2, getHeight() / 2 + 3);

            g2d.setColor(TEXT_SECONDARY);
            g2d.setFont(new Font("Inter", Font.BOLD, 11)); // Giảm font size từ 12 xuống 11
            fm = g2d.getFontMetrics();
            int labelWidth = fm.stringWidth(label.toUpperCase());
            g2d.drawString(label.toUpperCase(), (getWidth() - labelWidth) / 2, getHeight() / 2 + 22);
        }
    }

    private class ModernCard extends JPanel {

        private String iconPath, title, description;
        private int count, index;
        private boolean isHovered = false;
        private float hoverProgress = 0f;
        private float alpha = 0.0f; // Bắt đầu với alpha = 0
        private Timer hoverTimer;
        private FlatSVGIcon svgIcon;

        public ModernCard(String iconPath, String title, String description, int count, int index) {
            this.iconPath = iconPath;
            this.title = title;
            this.description = description;
            this.count = count;
            this.index = index;
            this.alpha = 0.0f; // Đảm bảo bắt đầu với alpha = 0

            // Tạo SVG icon với kích thước 50x50
            try {
                this.svgIcon = new FlatSVGIcon("./Icons/" + iconPath, 50, 50);
            } catch (Exception e) {
                System.err.println("Không thể tải icon: " + iconPath);
                this.svgIcon = null;
            }

            setOpaque(false);
            setPreferredSize(new Dimension(300, 200));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    if (alpha > 0.8f) { // Chỉ cho phép hover khi card đã hiển thị gần như hoàn toàn
                        isHovered = true;
                        startHoverAnimation();
                    }
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    isHovered = false;
                    startHoverAnimation();
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    if (alpha > 0.8f) { // Chỉ cho phép click khi card đã hiển thị
                        openDialog(index);
                    }
                }
            });
        }

        // Thêm method để set alpha ban đầu
        public void setInitialAlpha(float alpha) {
            this.alpha = alpha;
            repaint();
        }

        public void updateCount(int newCount) {
            this.count = newCount;
            repaint();
        }

        public void startFadeIn() {
            // Đảm bảo alpha bắt đầu từ 0
            this.alpha = 0.0f;
            
            Timer fadeIn = new Timer(25, new ActionListener() { // Giảm interval để animation mượt hơn
                public void actionPerformed(ActionEvent e) {
                    alpha = Math.min(1.0f, alpha + 0.08f); // Giảm increment để animation chậm hơn
                    repaint();
                    if (alpha >= 1.0f) {
                        ((Timer) e.getSource()).stop();
                    }
                }
            });
            fadeIn.start();
        }

        private void startHoverAnimation() {
            if (hoverTimer != null && hoverTimer.isRunning()) {
                hoverTimer.stop();
            }

            hoverTimer = new Timer(16, new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (isHovered && hoverProgress < 1f) {
                        hoverProgress += 0.1f;
                        if (hoverProgress > 1f) {
                            hoverProgress = 1f;
                        }
                        repaint();
                    } else if (!isHovered && hoverProgress > 0f) {
                        hoverProgress -= 0.1f;
                        if (hoverProgress < 0f) {
                            hoverProgress = 0f;
                        }
                        repaint();
                    }

                    if (hoverProgress == 0f || hoverProgress == 1f) {
                        ((Timer) e.getSource()).stop();
                    }
                }
            });
            hoverTimer.start();
        }

        @Override
        protected void paintComponent(Graphics g) {
            // Chỉ vẽ khi alpha > 0
            if (alpha <= 0.0f) {
                return;
            }
            
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));

            int shadowOffset = (int) (hoverProgress * 3);
            int elevation = (int) (hoverProgress * 6);

            // Shadow
            if (isHovered && alpha > 0.8f) {
                g2d.setColor(new Color(0, 0, 0, (int)(30 * alpha)));
                g2d.fillRoundRect(2, 6, getWidth() - 4, getHeight() - 8, 24, 24);
            } else {
                g2d.setColor(new Color(0, 0, 0, (int)(10 * alpha)));
                g2d.fillRoundRect(1, 2, getWidth() - 2, getHeight() - 4, 24, 24);
            }

            // Card background
            g2d.setColor(CARD_BACKGROUND);
            g2d.fillRoundRect(0, 0, getWidth() - shadowOffset, getHeight() - shadowOffset - elevation, 24, 24);

            // Hover border
            if (hoverProgress > 0 && alpha > 0.8f) {
                g2d.setColor(new Color(
                        ACCENT_COLOR.getRed(),
                        ACCENT_COLOR.getGreen(),
                        ACCENT_COLOR.getBlue(),
                        (int) (hoverProgress * 255 * alpha)
                ));
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRoundRect(
                        0,
                        0,
                        getWidth() - shadowOffset - 1,
                        getHeight() - shadowOffset - elevation - 1,
                        24,
                        24
                );
            }

            // Count badge
            g2d.setColor(new Color(102, 126, 234, (int)(50 * alpha)));
            g2d.fillRoundRect(getWidth() - 80, 15, 65, 25, 20, 20);
            g2d.setColor(new Color(ACCENT_COLOR.getRed(), ACCENT_COLOR.getGreen(), ACCENT_COLOR.getBlue(), (int)(255 * alpha)));
            g2d.setFont(new Font("Inter", Font.BOLD, 11));
            g2d.drawString(count + " mục", getWidth() - 75, 30);

            // SVG Icon
            if (svgIcon != null) {
                int iconX = (getWidth() - svgIcon.getIconWidth()) / 2;
                int iconY = 40;
                
                // Tạo composite cho icon với alpha
                Composite originalComposite = g2d.getComposite();
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
                svgIcon.paintIcon(this, g2d, iconX, iconY);
                g2d.setComposite(originalComposite);
            }

            // Title
            g2d.setColor(new Color(TEXT_PRIMARY.getRed(), TEXT_PRIMARY.getGreen(), TEXT_PRIMARY.getBlue(), (int)(255 * alpha)));
            g2d.setFont(new Font("Inter", Font.BOLD, 18));
            FontMetrics fm = g2d.getFontMetrics();
            int titleWidth = fm.stringWidth(title);
            g2d.drawString(title, (getWidth() - titleWidth) / 2, 115);

            // Description
            g2d.setColor(new Color(TEXT_SECONDARY.getRed(), TEXT_SECONDARY.getGreen(), TEXT_SECONDARY.getBlue(), (int)(255 * alpha)));
            g2d.setFont(new Font("Inter", Font.PLAIN, 13));
            drawCenteredString(g2d, description, getWidth() / 2, 140, getWidth() - 40);
        }

        private void drawCenteredString(Graphics2D g2d, String text, int x, int y, int width) {
            FontMetrics fm = g2d.getFontMetrics();
            String[] words = text.split(" ");
            StringBuilder line = new StringBuilder();
            int lineHeight = fm.getHeight();
            int currentY = y;

            for (String word : words) {
                String testLine = line.length() > 0 ? line + " " + word : word;
                if (fm.stringWidth(testLine) > width && line.length() > 0) {
                    int lineWidth = fm.stringWidth(line.toString());
                    g2d.drawString(line.toString(), x - lineWidth / 2, currentY);
                    line = new StringBuilder(word);
                    currentY += lineHeight;
                } else {
                    line = new StringBuilder(testLine);
                }
            }
            if (line.length() > 0) {
                int lineWidth = fm.stringWidth(line.toString());
                g2d.drawString(line.toString(), x - lineWidth / 2, currentY);
            }
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }
    }

    private void openDialog(int index) {
        JFrame owner = (JFrame) SwingUtilities.getWindowAncestor(this);

        switch (index) {
            case 0:
                new ThuongHieuDialog(owner, this, "Quản lý thương hiệu", true, m.user.getManhomquyen()).setVisible(true);
                break;
            case 1:
                new XuatXuDialog(owner, this, "Quản lý xuất xứ sản phẩm", true, m.user.getManhomquyen()).setVisible(true);
                break;
            case 2:
                new HeDieuHanhDialog(owner, this, "Quản lý hệ điều hành", true, m.user.getManhomquyen()).setVisible(true);
                break;
            case 3:
                new DungLuongRamDialog(owner, this, "Quản lý dung lượng RAM", true, m.user.getManhomquyen()).setVisible(true);
                break;
            case 4:
                new DungLuongRomDialog(owner, this, "Quản lý dung lượng ROM", true, m.user.getManhomquyen()).setVisible(true);
                break;
            case 5:
                new MauSacDialog(owner, this, "Quản lý màu sắc", true, m.user.getManhomquyen()).setVisible(true);
                break;
        }

        // Refresh dữ liệu sau khi đóng dialog
        SwingUtilities.invokeLater(() -> {
            refreshData();
            });
    }
}