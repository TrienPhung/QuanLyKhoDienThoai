package View;

import Model.DAO.TaiKhoanDAO;
import Model.DTO.TaiKhoanDTO;
import View.Component.InputForm;
import View.Dialog.QuenMatKhau;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import Helper.BCrypt;
import Utils.SessionManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Log_In extends JFrame implements KeyListener {

    JPanel pnlMain, pnlLogIn, pnlLeft, pnlRight;
    JLabel lblImage, lblTitle, lblSubtitle, lblLoginText, lblForgotPassword;
    InputForm txtUsername, txtPassword;

    private static final Color PRIMARY_COLOR = new Color(74, 144, 226);
    private static final Color SECONDARY_COLOR = new Color(245, 247, 250);
    private static final Color TEXT_COLOR = new Color(52, 73, 94);
    private static final Color HOVER_COLOR = new Color(62, 122, 194);
    private static final Color PLACEHOLDER_COLOR = new Color(149, 165, 166);

    public Log_In() {
        initComponent();
    }

    private void initComponent() {
        this.setSize(new Dimension(1000, 600));
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());
        this.setTitle("Đăng nhập");
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JFrame jf = this;

        pnlMain = new JPanel(new BorderLayout());
        pnlMain.setBackground(Color.WHITE);

        setupLeftPanel();
        setupRightPanel(jf);

        pnlMain.add(pnlLeft, BorderLayout.WEST);
        pnlMain.add(pnlRight, BorderLayout.CENTER);
        this.add(pnlMain);
    }

    private void setupLeftPanel() {
        pnlLeft = new JPanel();
        pnlLeft.setBackground(SECONDARY_COLOR);
        pnlLeft.setPreferredSize(new Dimension(500, 600));
        pnlLeft.setLayout(new BorderLayout());
        pnlLeft.setBorder(new EmptyBorder(50, 50, 50, 50));

        JPanel imageContainer = new JPanel(new BorderLayout());
        imageContainer.setBackground(SECONDARY_COLOR);

        lblImage = new JLabel();
        try {
            lblImage.setIcon(new FlatSVGIcon("./Images/login.svg"));
        } catch (Exception e) {
            lblImage.setText("<html><div style='text-align: center; font-size: 72px; color: #4A90E2;'>🔐</div></html>");
        }
        lblImage.setHorizontalAlignment(SwingConstants.CENTER);
        imageContainer.add(lblImage, BorderLayout.CENTER);
//        JLabel welcomeLabel = new JLabel("<html><div style='text-align: center;'>" +
//                "<div style='font-size: 24px; color: #4A90E2; font-weight: bold;'>Hệ Thống Quản Lý</div>" +
//                "<div style='font-size: 16px; color: #95A5A6;'>Đăng nhập an toàn và tiện lợi</div>" +
//                "</div></html>");
//        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
//        imageContainer.add(welcomeLabel, BorderLayout.SOUTH);

        pnlLeft.add(imageContainer, BorderLayout.CENTER);
    }

    private void setupRightPanel(JFrame jf) {
        pnlRight = new JPanel();
        pnlRight.setBackground(Color.WHITE);
        pnlRight.setPreferredSize(new Dimension(500, 600));
        pnlRight.setLayout(new BoxLayout(pnlRight, BoxLayout.Y_AXIS));
        pnlRight.setBorder(new EmptyBorder(50, 60, 50, 60));

        lblTitle = new JLabel("ĐĂNG NHẬP VÀO HỆ THỐNG");
        lblTitle.setFont(new Font(FlatRobotoFont.FAMILY_SEMIBOLD, Font.BOLD, 24));
        lblTitle.setForeground(TEXT_COLOR);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblSubtitle = new JLabel("Chào mừng bạn trở lại!");
        lblSubtitle.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 16));
        lblSubtitle.setForeground(PLACEHOLDER_COLOR);
        lblSubtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        pnlRight.add(lblTitle);
        pnlRight.add(Box.createVerticalStrut(5));
        pnlRight.add(lblSubtitle);
        pnlRight.add(Box.createVerticalStrut(30));

        // Tăng chiều cao của input từ 60 lên 80
        Dimension inputSize = new Dimension(360, 100);

        txtUsername = new InputForm("Tên đăng nhập");
        txtPassword = new InputForm("Mật khẩu", "password");

        styleInputField(txtUsername, inputSize);
        styleInputField(txtPassword, inputSize);

        txtUsername.getTxtForm().addKeyListener(this);
        txtPassword.getTxtPass().addKeyListener(this);

        txtUsername.setPreferredSize(inputSize);
        txtUsername.setMaximumSize(inputSize);
        txtUsername.setAlignmentX(Component.CENTER_ALIGNMENT);

        txtPassword.setPreferredSize(inputSize);
        txtPassword.setMaximumSize(inputSize);
        txtPassword.setAlignmentX(Component.CENTER_ALIGNMENT);

        pnlRight.add(txtUsername);
        pnlRight.add(Box.createVerticalStrut(0));
        pnlRight.add(txtPassword);
        pnlRight.add(Box.createVerticalStrut(25));

        setupLoginButton(jf);
        // Đặt kích thước nút đăng nhập bằng với input (360x60)
        pnlLogIn.setMaximumSize(new Dimension(360, 55));
        pnlLogIn.setAlignmentX(Component.CENTER_ALIGNMENT);
        pnlRight.add(pnlLogIn);
        pnlRight.add(Box.createVerticalStrut(20));

        lblForgotPassword = new JLabel("Quên mật khẩu?");
        lblForgotPassword.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 14));
        lblForgotPassword.setForeground(PRIMARY_COLOR);
        lblForgotPassword.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblForgotPassword.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblForgotPassword.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                QuenMatKhau qmk = new QuenMatKhau(jf, true);
                qmk.setVisible(true);
            }

            @Override
            public void mouseEntered(MouseEvent evt) {
                lblForgotPassword.setForeground(HOVER_COLOR);
            }

            @Override
            public void mouseExited(MouseEvent evt) {
                lblForgotPassword.setForeground(PRIMARY_COLOR);
            }
        });

        pnlRight.add(lblForgotPassword);
    }

    private void styleInputField(InputForm inputForm, Dimension size) {
        JComponent component = inputForm.getTxtForm() != null ? inputForm.getTxtForm() : inputForm.getTxtPass();
        if (component != null) {
            component.putClientProperty(FlatClientProperties.STYLE,
                    "arc: 8; borderWidth: 1; focusedBorderColor: #4A90E2; borderColor: #dcdde1");
            component.setPreferredSize(size);
            component.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 14));
        }
    }

    private void setupLoginButton(JFrame jf) {
        lblLoginText = new JLabel("ĐĂNG NHẬP");
        lblLoginText.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 16));
        lblLoginText.setForeground(Color.WHITE);
        lblLoginText.setHorizontalAlignment(SwingConstants.CENTER);
        lblLoginText.setVerticalAlignment(SwingConstants.CENTER);

        pnlLogIn = new JPanel(new BorderLayout());
        pnlLogIn.putClientProperty(FlatClientProperties.STYLE, "arc: 99");
        pnlLogIn.setBackground(PRIMARY_COLOR);
        // Đặt kích thước nút đăng nhập bằng với input (360x60)
        pnlLogIn.setPreferredSize(new Dimension(360, 55));
        pnlLogIn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        pnlLogIn.add(lblLoginText, BorderLayout.CENTER);

        pnlLogIn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) {
                pnlLogIn.setBackground(HOVER_COLOR);
            }

            @Override
            public void mousePressed(MouseEvent evt) {
                try {
                    checkLogin();
                } catch (UnsupportedLookAndFeelException ex) {
                    Logger.getLogger(Log_In.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            @Override
            public void mouseExited(MouseEvent evt) {
                pnlLogIn.setBackground(PRIMARY_COLOR);
            }
        });
    }

    public void checkLogin() throws UnsupportedLookAndFeelException {
        String usernameCheck = txtUsername.getText();
        String passwordCheck = txtPassword.getPass();
        if (usernameCheck.equals("") || passwordCheck.equals("")) {
            showModernMessage("Vui lòng nhập thông tin đầy đủ", "Cảnh báo!", JOptionPane.WARNING_MESSAGE);
        } else {
            TaiKhoanDTO tk = TaiKhoanDAO.getInstance().selectByUser(usernameCheck);
            if (tk == null) {
                showModernMessage("Tài khoản không tồn tại", "Cảnh báo!", JOptionPane.WARNING_MESSAGE);
            } else if (tk.getTrangthai() == 0) {
                showModernMessage("Tài khoản bị khóa", "Cảnh báo!", JOptionPane.WARNING_MESSAGE);
            } else if (BCrypt.checkpw(passwordCheck, tk.getMatkhau())) {
                JOptionPane.showMessageDialog(this,"Đăng nhập thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                SessionManager.setLoggedIn(true);
                this.dispose();
                Main main = new Main(tk);
                main.setVisible(true);
            } else {
                showModernMessage("Mật khẩu không đúng", "Cảnh báo!", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private void showModernMessage(String message, String title, int messageType) {
        JLabel messageLabel = new JLabel("<html><div style='text-align: center; padding: 10px;'>" + message + "</div></html>");
        messageLabel.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 14));
        messageLabel.setForeground(TEXT_COLOR);
        JOptionPane.showMessageDialog(this, messageLabel, title, messageType);
    }

    public static void main(String[] args) {
        try {
            FlatRobotoFont.install();
            FlatLaf.setPreferredFontFamily(FlatRobotoFont.FAMILY);
            FlatLaf.setPreferredLightFontFamily(FlatRobotoFont.FAMILY_LIGHT);
            FlatLaf.setPreferredSemiboldFontFamily(FlatRobotoFont.FAMILY_SEMIBOLD);
            FlatIntelliJLaf.registerCustomDefaultsSource("style");
            FlatIntelliJLaf.setup();
            UIManager.put("PasswordField.showRevealButton", true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            Log_In login = new Log_In();
            login.setVisible(true);
        });
    }

    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            try {
                checkLogin();
            } catch (UnsupportedLookAndFeelException ex) {
                Logger.getLogger(Log_In.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    @Override
    public void keyReleased(KeyEvent e) {}
}