package View.Dialog;

import View.Component.HeaderTitle;
import View.Component.InputForm;
import View.Component.ButtonCustom;
import Model.DAO.KhachHangDAO;
import Model.DTO.KhachHangDTO;
import View.Panel.KhachHang;
import Model.BUS.KhachHangBUS;
import View.Component.NumericDocumentFilter;
import Helper.Validation;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.PlainDocument;
import org.apache.commons.codec.language.bm.Rule;

public class KhachHangDialog extends JDialog implements MouseListener {

    KhachHang jpKH;
    private HeaderTitle titlePage;
    private JPanel pnlMain, pnlButtom;
    private ButtonCustom btnThem, btnCapNhat, btnHuyBo;
    private InputForm tenKH, sdtKH, diachiKH;
    private JTextField maKH;
    KhachHangDTO kh;

    public KhachHangDialog(KhachHang jpKH, JFrame owner, String title, boolean modal, String type) {
        super(owner, title, modal);
        this.jpKH = jpKH;
        tenKH = new InputForm("Tên khách hàng");
        sdtKH = new InputForm("Số điện thoại");
        PlainDocument phonex = (PlainDocument) sdtKH.getTxtForm().getDocument();
        phonex.setDocumentFilter((new NumericDocumentFilter()));
        diachiKH = new InputForm("Địa chỉ");
        initComponents(title, type);
    }

    public KhachHangDialog(KhachHang jpKH, JFrame owner, String title, boolean modal, String type, KhachHangDTO kh) {
        super(owner, title, modal);
        this.kh = kh;
        maKH = new JTextField("");
        setMaKH(Integer.toString(kh.getMaKH()));
        tenKH = new InputForm("Tên khách hàng");
        setTenKH(kh.getHoten());
        sdtKH = new InputForm("Số điện thoại");
        setSdtKH(kh.getSdt());
        diachiKH = new InputForm("Địa chỉ");
        setDiaChiKH(kh.getDiachi());
        this.jpKH = jpKH;
        initComponents(title, type);
    }

    public void initComponents(String title, String type) {
        this.setSize(new Dimension(500, 500));
        this.setLayout(new BorderLayout(0, 0));
        titlePage = new HeaderTitle(title.toUpperCase());
        pnlMain = new JPanel(new GridLayout(3, 1, 20, 0));
        pnlMain.setBackground(Color.white);

        pnlMain.add(tenKH);
        pnlMain.add(sdtKH);
        pnlMain.add(diachiKH);

        pnlButtom = new JPanel(new FlowLayout());
        pnlButtom.setBorder(new EmptyBorder(10, 0, 10, 0));
        pnlButtom.setBackground(Color.white);
        btnThem = new ButtonCustom("Thêm khách hàng", "success", 14);
        btnCapNhat = new ButtonCustom("Lưu thông tin", "success", 14);
        btnHuyBo = new ButtonCustom("Huỷ bỏ", "danger", 14);

        //Add MouseListener btn
        btnThem.addMouseListener(this);
        btnCapNhat.addMouseListener(this);
        btnHuyBo.addMouseListener(this);

        switch (type) {
            case "create" ->
                pnlButtom.add(btnThem);
            case "update" ->
                pnlButtom.add(btnCapNhat);
            case "view" -> {
                tenKH.setDisable();
                sdtKH.setDisable();
                diachiKH.setDisable();
            }
            default ->
                throw new AssertionError();
        }
        pnlButtom.add(btnHuyBo);

        this.add(titlePage, BorderLayout.NORTH);
        this.add(pnlMain, BorderLayout.CENTER);
        this.add(pnlButtom, BorderLayout.SOUTH);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public void setTenKH(String name) {
        tenKH.setText(name);
    }

    public String getTenKH() {
        return tenKH.getText();
    }

    public String getMaKH() {
        return maKH.getText();
    }

    public void setMaKH(String id) {
        this.maKH.setText(id);
    }

    public String getSdtKH() {
        return sdtKH.getText();
    }

    public void setSdtKH(String id) {
        this.sdtKH.setText(id);
    }

    public String getDiaChiKH() {
        return diachiKH.getText();
    }

    public void setDiaChiKH(String id) {
        this.diachiKH.setText(id);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
//        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    boolean Validation() {
        String ten = tenKH.getText().trim();
        String sdt = sdtKH.getText().trim();
        String diachi = diachiKH.getText().trim();

        // Regex kiểm tra: tên chỉ chứa chữ cái và khoảng trắng
        if (Validation.isEmpty(ten)) {
            JOptionPane.showMessageDialog(this, "Tên khách hàng không được rỗng", "Cảnh báo !", JOptionPane.WARNING_MESSAGE);
            return false;
        } else if (!ten.matches("^[\\p{L}\\s]+$")) {
            JOptionPane.showMessageDialog(this, "Tên khách hàng không được chứa ký tự đặc biệt hoặc số", "Cảnh báo !", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        // Số điện thoại: đúng 10 chữ số
        if (Validation.isEmpty(sdt)) {
            JOptionPane.showMessageDialog(this, "Số điện thoại không được rỗng", "Cảnh báo !", JOptionPane.WARNING_MESSAGE);
            return false;
        } else if (!sdt.matches("^\\d{10}$")) {
            JOptionPane.showMessageDialog(this, "Số điện thoại phải là 10 chữ số và không chứa ký tự đặc biệt", "Cảnh báo !", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        // Địa chỉ: không rỗng, không toàn ký tự đặc biệt
        if (Validation.isEmpty(diachi)) {
            JOptionPane.showMessageDialog(this, "Địa chỉ không được rỗng", "Cảnh báo !", JOptionPane.WARNING_MESSAGE);
            return false;
        } else if (!diachi.matches(".*[\\p{L}\\d].*")) {
            JOptionPane.showMessageDialog(this, "Địa chỉ không hợp lệ, không được toàn ký tự đặc biệt", "Cảnh báo !", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        return true;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getSource() == btnThem && Validation()) {
            int id = KhachHangDAO.getInstance().getAutoIncrement();
            boolean added = jpKH.khachhangBUS.add(new KhachHangDTO(id, tenKH.getText(), sdtKH.getText(), diachiKH.getText()));
            if (added) {
                jpKH.loadDataTable(jpKH.listkh);
                JOptionPane.showMessageDialog(this, "Thêm khách hàng thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Không thể thêm khách hàng! Có thể do trùng thông tin hoặc lỗi hệ thống.", "Thất bại", JOptionPane.ERROR_MESSAGE);
            }

        } else if (e.getSource() == btnHuyBo) {
            dispose();
        } else if (e.getSource() == btnCapNhat && Validation()) {
            boolean updated = jpKH.khachhangBUS.update(new KhachHangDTO(kh.getMaKH(), tenKH.getText(), sdtKH.getText(), diachiKH.getText()));
            if (updated) {
                jpKH.loadDataTable(jpKH.listkh);
                JOptionPane.showMessageDialog(this, "Cập nhật khách hàng thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Không thể cập nhật khách hàng! Có thể do lỗi hệ thống.", "Thất bại", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static boolean isPhoneNumber(String str) {
        // Loại bỏ khoảng trắng và dấu ngoặc đơn nếu có
        str = str.replaceAll("\\s+", "").replaceAll("\\(", "").replaceAll("\\)", "").replaceAll("\\-", "");

        // Kiểm tra xem chuỗi có phải là một số điện thoại hợp lệ hay không
        if (str.matches("\\d{10}")) { // Kiểm tra số điện thoại 10 chữ số
            return true;
        } else if (str.matches("\\d{3}-\\d{3}-\\d{4}")) { // Kiểm tra số điện thoại có dấu gạch ngang
            return true;
        } else if (str.matches("\\(\\d{3}\\)\\d{3}-\\d{4}")) { // Kiểm tra số điện thoại có dấu ngoặc đơn
            return true;
        } else {
            return false; // Trả về false nếu chuỗi không phải là số điện thoại hợp lệ
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
//        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void mouseEntered(MouseEvent e) {
//        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void mouseExited(MouseEvent e) {
//        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
