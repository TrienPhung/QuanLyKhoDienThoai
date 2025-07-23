package View.Panel;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import com.formdev.flatlaf.FlatIntelliJLaf;

// PanelShadow Component
class PanelShadow extends JPanel {
    private int shadowSize = 10;
    private float shadowOpacity = 0.3f;
    private Color shadowColor = Color.BLACK;
    private int cornerRadius = 15;
    private boolean showShadow = true;
    
    public PanelShadow() {
        setOpaque(false);
        setBackground(Color.WHITE);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int width = getWidth();
        int height = getHeight();
        
        // Draw shadow
        if (showShadow) {
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, shadowOpacity));
            g2d.setColor(shadowColor);
            
            for (int i = 0; i < shadowSize; i++) {
                float opacity = shadowOpacity * (1.0f - (float) i / shadowSize);
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
                
                RoundRectangle2D.Float shadow = new RoundRectangle2D.Float(
                    i + 2, i + 2, 
                    width - (2 * i) - 4, height - (2 * i) - 4,
                    cornerRadius, cornerRadius
                );
                g2d.fill(shadow);
            }
        }
        
        // Draw main panel
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        g2d.setColor(getBackground());
        
        RoundRectangle2D.Float mainPanel = new RoundRectangle2D.Float(
            0, 0, 
            width - shadowSize - 2, height - shadowSize - 2,
            cornerRadius, cornerRadius
        );
        g2d.fill(mainPanel);
        
        // Draw subtle border
        g2d.setColor(new Color(230, 230, 230));
        g2d.setStroke(new BasicStroke(1));
        g2d.draw(mainPanel);
        
        g2d.dispose();
        super.paintComponent(g);
    }
    
    @Override
    public Insets getInsets() {
        return new Insets(5, 5, shadowSize + 5, shadowSize + 5);
    }
    
    // Getter and Setter methods
    public void setShadowSize(int shadowSize) {
        this.shadowSize = shadowSize;
        repaint();
    }
    
    public void setShadowOpacity(float shadowOpacity) {
        this.shadowOpacity = Math.max(0.0f, Math.min(1.0f, shadowOpacity));
        repaint();
    }
    
    public void setShadowColor(Color shadowColor) {
        this.shadowColor = shadowColor;
        repaint();
    }
    
    public void setCornerRadius(int cornerRadius) {
        this.cornerRadius = cornerRadius;
        repaint();
    }
    
    public void setShowShadow(boolean showShadow) {
        this.showShadow = showShadow;
        repaint();
    }
}

// Main TrangChu Class
public class TrangChu extends JPanel {
    JPanel top, center, mainContent, rightPanel;
    PanelShadow content[];
    JPanel info[];
    JLabel title, subTit, infoDetail[], objDetail[], objDetail1[], infoIcon[];
    
    String[][] getSt = {
        {"TÍNH BẢO MẬT", "tinhbaomat_128px.svg", "Ứng tính bảo mật cho các hoạt động quản lý."},
        {"TÍNH CHÍNH XÁC", "tinhchinhxac_128px.svg", "Đảm bảo tính chính xác và độ tin cậy cao của các hoạt động quản lý."},
        {"TÍNH HIỆU QUẢ", "tinhhieuqua_128px.svg", "Có thể dễ dàng xác định được thông tin về từng thiết bị điện thoại một cách nhanh chóng và chính xác."},
        {"TÍNH ỔN ĐỊNH", "tinhondinh_128px.svg", "Hệ thống có khả năng hoạt động ổn định theo thời gian dài và ít xảy ra các lỗi trang lỗi."}
    };
    
    Color MainColor = new Color(255, 255, 255);
    Color FontColor = new Color(96, 125, 139);
    Color BackgroundColor = new Color(240, 247, 250);
    Color AccentColor = new Color(0, 188, 212); // Cyan color from design
    Color SecondaryColor = new Color(33, 150, 243); // Blue color
    
    private void initComponent() {
        this.setBackground(BackgroundColor);
        this.setLayout(new BorderLayout(0, 0));
        this.setOpaque(true);
        
        // Create main content panel
        mainContent = new JPanel(new BorderLayout());
        mainContent.setBackground(BackgroundColor);
        
        // Top section with title and illustration
        createTopSection();
        
        // Center section with feature cards
        createCenterSection();
        
        this.add(mainContent, BorderLayout.CENTER);
    }
    
    private void createTopSection() {
        top = new JPanel();
        top.setBackground(BackgroundColor);
        top.setPreferredSize(new Dimension(1100, 350));
        top.setLayout(new BorderLayout());
        top.setBorder(new EmptyBorder(40, 50, 40, 50));
        
        // Left side with text
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(BackgroundColor);
        leftPanel.setPreferredSize(new Dimension(500, 300));
        
        // Main title
        JLabel mainTitle = new JLabel("HỆ THỐNG QUẢN");
        mainTitle.setFont(new Font("Arial", Font.BOLD, 48));
        mainTitle.setForeground(AccentColor);
        mainTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel subTitle = new JLabel("LÝ ĐIỆN THOẠI");
        subTitle.setFont(new Font("Arial", Font.BOLD, 48));
        subTitle.setForeground(AccentColor);
        subTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        leftPanel.add(Box.createVerticalStrut(50));
        leftPanel.add(mainTitle);
        leftPanel.add(Box.createVerticalStrut(10));
        leftPanel.add(subTitle);
        leftPanel.add(Box.createVerticalGlue());
        
        // Right side with illustration
        rightPanel = new JPanel();
        rightPanel.setBackground(BackgroundColor);
        rightPanel.setPreferredSize(new Dimension(600, 300));
        rightPanel.setLayout(new BorderLayout());
        
        // Create illustration panel with geometric design
        JPanel illustrationPanel = createIllustrationPanel();
        rightPanel.add(illustrationPanel, BorderLayout.CENTER);
        
        top.add(leftPanel, BorderLayout.WEST);
        top.add(rightPanel, BorderLayout.EAST);
        
        mainContent.add(top, BorderLayout.NORTH);
    }
    
    private JPanel createIllustrationPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int width = getWidth();
                int height = getHeight();
                
                // Draw geometric shapes inspired by the design
                // Background gradient rectangles
                GradientPaint gradient1 = new GradientPaint(0, 0, new Color(0, 188, 212, 200), 
                                                           width, 0, new Color(0, 188, 212, 100));
                g2d.setPaint(gradient1);
                g2d.fillRect(width/3, 0, width*2/3, height/2);
                
                GradientPaint gradient2 = new GradientPaint(0, height/2, new Color(33, 150, 243, 180), 
                                                           width, height, new Color(33, 150, 243, 80));
                g2d.setPaint(gradient2);
                g2d.fillRect(width/4, height/3, width*3/4, height*2/3);
                
                // Draw phone mockup
                g2d.setColor(Color.WHITE);
                int phoneX = width/2 - 60;
                int phoneY = height/2 - 100;
                g2d.fillRoundRect(phoneX, phoneY, 120, 200, 20, 20);
                
                // Phone screen
                g2d.setColor(new Color(240, 240, 240));
                g2d.fillRoundRect(phoneX + 10, phoneY + 30, 100, 140, 10, 10);
                
                // Draw person silhouette
                g2d.setColor(new Color(33, 150, 243));
                int personX = phoneX - 80;
                int personY = phoneY + 120;
                // Head
                g2d.fillOval(personX + 20, personY, 20, 20);
                // Body
                g2d.fillRect(personX + 25, personY + 20, 10, 30);
                // Arms
                g2d.fillRect(personX + 10, personY + 25, 15, 5);
                g2d.fillRect(personX + 35, personY + 25, 15, 5);
                // Legs
                g2d.fillRect(personX + 20, personY + 50, 5, 20);
                g2d.fillRect(personX + 30, personY + 50, 5, 20);
                
                // Draw decorative dots
                g2d.setColor(Color.WHITE);
                for (int i = 0; i < 20; i++) {
                    int dotX = (int)(Math.random() * width);
                    int dotY = (int)(Math.random() * height);
                    int dotSize = (int)(Math.random() * 8) + 3;
                    g2d.fillOval(dotX, dotY, dotSize, dotSize);
                }
                
                g2d.dispose();
            }
        };
        panel.setBackground(BackgroundColor);
        return panel;
    }
    
    private void createCenterSection() {
        center = new JPanel();
        center.setBackground(BackgroundColor);
        center.setLayout(new GridLayout(2, 2, 30, 30));
        center.setBorder(new EmptyBorder(50, 50, 50, 50));
        
        content = new PanelShadow[getSt.length];
        
        for (int i = 0; i < getSt.length; i++) {
            content[i] = createFeatureCard(getSt[i][0], getSt[i][2], getFeatureIcon(i));
            center.add(content[i]);
        }
        
        mainContent.add(center, BorderLayout.CENTER);
    }
    
    private PanelShadow createFeatureCard(String title, String description, Color iconColor) {
        PanelShadow card = new PanelShadow();
        card.setBackground(Color.WHITE);
        card.setLayout(new BorderLayout(15, 15));
        card.setBorder(new EmptyBorder(25, 25, 25, 25));
        
        // Icon panel
        JPanel iconPanel = new JPanel();
        iconPanel.setBackground(Color.WHITE);
        iconPanel.setPreferredSize(new Dimension(60, 60));
        iconPanel.setLayout(new BorderLayout());
        
        JLabel iconLabel = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Draw circle background
                g2d.setColor(iconColor);
                g2d.fillOval(5, 5, 50, 50);
                
                // Draw simple icon based on feature type
                g2d.setColor(Color.WHITE);
                g2d.setStroke(new BasicStroke(3));
                if (title.contains("BẢO MẬT")) {
                    // Shield icon
                    int[] xPoints = {30, 20, 30, 40};
                    int[] yPoints = {15, 35, 45, 35};
                    g2d.fillPolygon(xPoints, yPoints, 4);
                } else if (title.contains("CHÍNH XÁC")) {
                    // Target/bullseye icon
                    g2d.drawOval(15, 15, 30, 30);
                    g2d.drawOval(20, 20, 20, 20);
                    g2d.fillOval(25, 25, 10, 10);
                } else if (title.contains("HIỆU QUẢ")) {
                    // Chart/graph icon
                    g2d.drawLine(15, 40, 20, 30);
                    g2d.drawLine(20, 30, 25, 35);
                    g2d.drawLine(25, 35, 30, 20);
                    g2d.drawLine(30, 20, 35, 25);
                    g2d.drawLine(35, 25, 40, 15);
                } else {
                    // Stability/settings icon
                    g2d.drawOval(20, 20, 20, 20);
                    g2d.drawLine(30, 10, 30, 15);
                    g2d.drawLine(30, 45, 30, 50);
                    g2d.drawLine(10, 30, 15, 30);
                    g2d.drawLine(45, 30, 50, 30);
                }
                
                g2d.dispose();
            }
        };
        iconLabel.setPreferredSize(new Dimension(60, 60));
        iconPanel.add(iconLabel, BorderLayout.CENTER);
        
        // Text panel
        JPanel textPanel = new JPanel();
        textPanel.setBackground(Color.WHITE);
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(new Color(33, 33, 33));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel descLabel = new JLabel("<html>" + description + "</html>");
        descLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        descLabel.setForeground(new Color(117, 117, 117));
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        textPanel.add(titleLabel);
        textPanel.add(Box.createVerticalStrut(10));
        textPanel.add(descLabel);
        
        card.add(iconPanel, BorderLayout.WEST);
        card.add(textPanel, BorderLayout.CENTER);
        
        return card;
    }
    
    private Color getFeatureIcon(int index) {
        Color[] colors = {
            new Color(255, 193, 7),   // Yellow for security
            new Color(76, 175, 80),   // Green for accuracy  
            new Color(156, 39, 176),  // Purple for efficiency
            new Color(255, 87, 34)    // Orange for stability
        };
        return colors[index % colors.length];
    }
    
    public TrangChu() { 
        initComponent();
        FlatIntelliJLaf.registerCustomDefaultsSource("style");
        FlatIntelliJLaf.setup();
    }
}