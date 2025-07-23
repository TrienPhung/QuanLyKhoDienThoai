package View.Dialog;

import Model.DAO.TaiKhoanDAO;
import Model.DTO.TaiKhoanDTO;
import Helper.SendEmailSMTP;
import com.formdev.flatlaf.FlatLightLaf;
import Helper.BCrypt;
import Helper.Validation;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class QuenMatKhau extends JDialog implements ActionListener {

    private JButton btnSendMail, btnConfirmOTP, btnChangePass;
    private JPanel jpTop, jpMain, jpCard_1, jpCard_2, jpCard_3;
    private JLabel lblTitle, lblNhapEmail, lblNhapOTP, lblNhapPassword;
    private JTextField txtEmail, txtOTP;
    private JPasswordField txtPassword;
    private String emailCheck;

    public QuenMatKhau(Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setLocationRelativeTo(null);
    }

    public void initComponents() {
        this.setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
        this.setTitle("Quên mật khẩu");
        this.setSize(new Dimension(500, 200));
        this.setResizable(false);
        this.setLayout(new BorderLayout());

        jpTop = new JPanel(new BorderLayout());
        jpTop.setBackground(new Color(22, 122, 198));
        jpTop.setPreferredSize(new Dimension(400, 60));

        lblTitle = new JLabel();
        lblTitle.setFont(new Font("Segoe UI", 1, 18));
        lblTitle.setForeground(new Color(255, 255, 255));
        lblTitle.setHorizontalAlignment(JLabel.CENTER);
        lblTitle.setText("QUÊN MẬT KHẨU");
        lblTitle.setPreferredSize(new Dimension(400, 50));
        jpTop.add(lblTitle, BorderLayout.CENTER);

        jpMain = new JPanel();
        jpMain.setLayout(new CardLayout());

        // Step 1
        jpCard_1 = new JPanel(new FlowLayout(2, 10, 10));
        jpCard_1.setBackground(new Color(255, 255, 255));
        lblNhapEmail = new JLabel();
        lblNhapEmail.setText("Nhập địa chỉ email");
        lblNhapEmail.setHorizontalAlignment(Label.LEFT);
        txtEmail = new JTextField();
        txtEmail.setPreferredSize(new java.awt.Dimension(350, 35));

        btnSendMail = new JButton("Gửi mã");
        btnSendMail.setPreferredSize(new Dimension(100, 35));
        btnSendMail.addActionListener(this);
        jpCard_1.add(lblNhapEmail);
        jpCard_1.add(txtEmail);
        jpCard_1.add(btnSendMail);

        // Step 2
        jpCard_2 = new JPanel(new FlowLayout(2, 10, 10));
        jpCard_2.setBackground(new Color(255, 255, 255));
        lblNhapOTP = new JLabel();
        lblNhapOTP.setText("Nhập mã OTP");

        txtOTP = new JTextField();
        txtOTP.setPreferredSize(new java.awt.Dimension(350, 35));

        btnConfirmOTP = new JButton("Xác nhận");
        btnConfirmOTP.setPreferredSize(new Dimension(100, 35));
        btnConfirmOTP.addActionListener(this);
        jpCard_2.add(lblNhapOTP);
        jpCard_2.add(txtOTP);
        jpCard_2.add(btnConfirmOTP);

        // Step 3
        jpCard_3 = new JPanel(new FlowLayout(2, 10, 10));
        jpCard_3.setBackground(new Color(255, 255, 255));
        lblNhapPassword = new JLabel();
        lblNhapPassword.setText("Nhập mật khẩu mới");

        txtPassword = new JPasswordField();
        txtPassword.setPreferredSize(new java.awt.Dimension(350, 35));

        btnChangePass = new JButton("Xác nhận");
        btnChangePass.setPreferredSize(new Dimension(100, 35));
        btnChangePass.addActionListener(this);
        jpCard_3.add(lblNhapPassword);
        jpCard_3.add(txtPassword);
        jpCard_3.add(btnChangePass);

        jpMain.add(jpCard_1);
        jpMain.add(jpCard_2);
        jpMain.add(jpCard_3);

        this.getContentPane().add(jpTop, BorderLayout.NORTH);
        this.getContentPane().add(jpMain, BorderLayout.CENTER);

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == btnSendMail) {
//            c.next(jpMain);
            //Test pull github
            String email = txtEmail.getText().trim();
            if (email.equals("")) {
                JOptionPane.showMessageDialog(this, "Vui lòng không để trống email",
                        "Email không hợp lệ",
                        JOptionPane.ERROR_MESSAGE);
            } else {

                if (!Validation.isEmail(email)) {
                    JOptionPane.showMessageDialog(this, "Vui lòng nhập đúng định dạng email");
                } else {
                    TaiKhoanDTO tk = TaiKhoanDAO.getInstance().selectByEmail(email);
                    if (tk == null) {
                        JOptionPane.showMessageDialog(this,
                                "Tài khoản của email này không tồn tại trên hệ thống",
                                "Không tìm thấy tài khoản",
                                JOptionPane.ERROR_MESSAGE);
                    } else {

                        String opt = SendEmailSMTP.getOTP();
                        boolean emailSent = SendEmailSMTP.sendOTP(email, opt);

                        if (!emailSent) {
                            JOptionPane.showMessageDialog(
                                    this,
                                    "Không thể gửi mã OTP. Vui lòng thử lại sau.",
                                    "Lỗi gửi email",
                                    JOptionPane.ERROR_MESSAGE
                            );
                        } else {
                            CardLayout c = (CardLayout) jpMain.getLayout();
                            c.next(jpMain);
                            this.emailCheck = email;
                            TaiKhoanDAO.getInstance().sendOpt(email, opt);

                            JOptionPane.showMessageDialog(
                                    this,
                                    "Mã OTP đã được gửi tới email của bạn.",
                                    "Thành công",
                                    JOptionPane.INFORMATION_MESSAGE
                            );
                        }

                    }
                }
            }
        } else if (e.getSource() == btnConfirmOTP) {
            String otp = txtOTP.getText().trim();
            if (otp.equals("")) {
                JOptionPane.showMessageDialog(this, "Vui lòng không để trống mã OTP");
            } else {
                Pattern digitPattern = Pattern.compile("\\d{6}");
                Matcher matcher = digitPattern.matcher(otp);
                if (matcher.matches() == false) {
                    JOptionPane.showMessageDialog(this, "Vui lòng nhập mã OTP có 6 chữ số!");
                } else {
                    boolean check = TaiKhoanDAO.getInstance().checkOtp(this.emailCheck, otp);
                    if (check) {
                        CardLayout c = (CardLayout) jpMain.getLayout();
                        c.next(jpMain);
                    } else {
                        JOptionPane.showMessageDialog(this, "Mã OTP không chính xác.Vui lòng nhập lại!");
                    }
                }
            }
        } else if (e.getSource() == btnChangePass) {
            String pass = txtPassword.getText().trim();
            // Kiểm tra mật khẩu không hợp lệ
            if (pass.equals("")) {
                JOptionPane.showMessageDialog(
                        this,
                        "Mật khẩu không được để trống",
                        "Lỗi mật khẩu",
                        JOptionPane.ERROR_MESSAGE
                );
            } else if (pass.length() < 6) {
                JOptionPane.showMessageDialog(
                        this,
                        "Mật khẩu phải có ít nhất 6 ký tự",
                        "Lỗi mật khẩu",
                        JOptionPane.ERROR_MESSAGE
                );
            } else {
                // Nếu hợp lệ thì băm mật khẩu và cập nhật
                String password = BCrypt.hashpw(pass, BCrypt.gensalt(12));
                TaiKhoanDAO.getInstance().updatePass(this.emailCheck, password);

                // Xoá mã OTP đã sử dụng
                TaiKhoanDAO.getInstance().sendOpt(emailCheck, "null");

                JOptionPane.showMessageDialog(
                        this,
                        "Thay đổi mật khẩu thành công",
                        "Thành công",
                        JOptionPane.INFORMATION_MESSAGE
                );
                this.dispose(); // Đóng cửa sổ
            }

        }
    }
}
