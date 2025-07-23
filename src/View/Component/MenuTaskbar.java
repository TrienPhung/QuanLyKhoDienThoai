package View.Component;

import Model.DAO.ChiTietQuyenDAO;
import Model.DAO.NhanVienDAO;
import Model.DAO.NhomQuyenDAO;
import Model.DTO.ChiTietQuyenDTO;
import Model.DTO.NhanVienDTO;
import Model.DTO.NhomQuyenDTO;
import Model.DTO.TaiKhoanDTO;
import Utils.DbConnection;
import Utils.SessionManager;
import View.Log_In;
import View.Main;
import View.Panel.KhachHang;
import View.Panel.KhuVucKho;
import View.Panel.NhaCungCap;
import View.Panel.NhanVien;
import View.Panel.PhanQuyen;
import View.Panel.PhieuNhap;
import View.Panel.PhieuXuat;
import View.Panel.QuanLyThuocTinhSP;
import View.Panel.SanPham;
import View.Panel.TaiKhoan;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import View.Dialog.MyAccount;
import View.Panel.ThongKe.ThongKe;
import View.Panel.TrangChu;
import java.sql.*;
//import View.Component.itemTaskbar;

public class MenuTaskbar extends JPanel {

    TrangChu trangChu;
    SanPham sanPham;
    QuanLyThuocTinhSP quanLyThuocTinhSP;
    KhuVucKho quanLyKho;
    PhieuNhap phieuNhap;
    PhieuXuat phieuXuat;
    KhachHang khachHang;
    NhaCungCap nhacungcap;
    NhanVien nhanVien;
    TaiKhoan taiKhoan;
    PhanQuyen phanQuyen;
    ThongKe thongKe;
    String[][] getSt = {
        {"Trang chủ", "home.svg", "trangchu"},
        {"Sản phẩm", "product.svg", "sanpham"},
        {"Thuộc tính", "brand.svg", "thuoctinh"},
        {"Khu vực kho", "area.svg", "khuvuckho"},
        {"Phiếu nhập", "import.svg", "nhaphang"},
        {"Phiếu xuất", "export.svg", "xuathang"},
        {"Khách hàng", "customer.svg", "khachhang"},
        {"Nhà cung cấp", "supplier.svg", "nhacungcap"},
        {"Nhân viên", "staff.svg", "nhanvien"},
        {"Tài khoản", "account.svg", "taikhoan"},
        {"Thống kê", "statistical.svg", "thongke"},
        {"Phân quyền", "permission.svg", "nhomquyen"},
        {"Đăng xuất", "log_out.svg", "dangxuat"},};

    Main main;
    TaiKhoanDTO user;
    public itemTaskbar[] listitem;

    JLabel lblTenNhomQuyen, lblUsername;
    JScrollPane scrollPane;

    //tasbarMenu chia thành 3 phần chính là pnlCenter, pnlTop, pnlBottom
    JPanel pnlCenter, pnlTop, pnlBottom, bar1, bar2, bar3, bar4;

    Color FontColor = new Color(96, 125, 139);
    Color DefaultColor = new Color(255, 255, 255);
    Color HowerFontColor = new Color(1, 87, 155);
    Color HowerBackgroundColor = new Color(33, 150, 243, 55);//new Color(33, 150, 243);
    private ArrayList<ChiTietQuyenDTO> listQuyen;
    NhomQuyenDTO nhomQuyenDTO;
    public NhanVienDTO nhanVienDTO;
    JFrame owner = (JFrame) SwingUtilities.getWindowAncestor(this);

    public MenuTaskbar(Main main) {
        this.main = main;
        initComponent();
    }

    public MenuTaskbar(Main main, TaiKhoanDTO tk) {
        this.main = main;
        // ✅ Kiểm tra trạng thái đăng nhập
        if (!SessionManager.isLoggedIn()) {
            JOptionPane.showMessageDialog(null,
                    "Vui lòng đăng nhập để tiếp tục sử dụng hệ thống.",
                    "Yêu cầu đăng nhập", JOptionPane.WARNING_MESSAGE);

            main.dispose();
            SwingUtilities.invokeLater(() -> new Log_In().setVisible(true));
            return; // Dừng tạo giao diện nếu chưa đăng nhập
        }
        this.user = tk;
        this.nhomQuyenDTO = NhomQuyenDAO.getInstance().selectById(Integer.toString(tk.getManhomquyen()));
        this.nhanVienDTO = NhanVienDAO.getInstance().selectById(Integer.toString(tk.getManv()));
        listQuyen = ChiTietQuyenDAO.getInstance().selectAll(Integer.toString(tk.getManhomquyen()));
        initComponent();
    }

    private void initComponent() {
        listitem = new itemTaskbar[getSt.length];
        this.setOpaque(true);
        this.setBackground(DefaultColor);
        this.setLayout(new BorderLayout(0, 0));

        // bar1, bar là các đường kẻ mỏng giữa taskbarMenu và MainContent
        pnlTop = new JPanel();
        pnlTop.setPreferredSize(new Dimension(250, 80));
        pnlTop.setBackground(DefaultColor);
        pnlTop.setLayout(new BorderLayout(0, 0));
        this.add(pnlTop, BorderLayout.NORTH);

        JPanel info = new JPanel();
        info.setOpaque(false);
        info.setLayout(new BorderLayout(0, 0));
        pnlTop.add(info, BorderLayout.CENTER);

        // Cái info này bỏ vô cho đẹp tí, mai mốt có gì xóa đi đê hiển thị thông tin tài khoản và quyền
        in4(info);

        bar1 = new JPanel();
        bar1.setBackground(new Color(204, 214, 219));
        bar1.setPreferredSize(new Dimension(1, 0));
        pnlTop.add(bar1, BorderLayout.EAST);

        bar2 = new JPanel();
        bar2.setBackground(new Color(204, 214, 219));
        bar2.setPreferredSize(new Dimension(0, 1));
        pnlTop.add(bar2, BorderLayout.SOUTH);

        pnlCenter = new JPanel();
        pnlCenter.setPreferredSize(new Dimension(230, 600));
        pnlCenter.setBackground(DefaultColor);
//        pnlCenter.setBorder(new EmptyBorder(0,15,0,35));
        pnlCenter.setLayout(new FlowLayout(0, 0, 5));

        bar3 = new JPanel();
        bar3.setBackground(new Color(204, 214, 219));
        bar3.setPreferredSize(new Dimension(1, 1));
        this.add(bar3, BorderLayout.EAST);

        scrollPane = new JScrollPane(pnlCenter, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(new EmptyBorder(5, 10, 0, 10));
        this.add(scrollPane, BorderLayout.CENTER);

        pnlBottom = new JPanel();
        pnlBottom.setPreferredSize(new Dimension(250, 50));
        pnlBottom.setBackground(DefaultColor);
        pnlBottom.setLayout(new BorderLayout(0, 0));

        bar4 = new JPanel();
        bar4.setBackground(new Color(204, 214, 219));
        bar4.setPreferredSize(new Dimension(1, 1));
        pnlBottom.add(bar4, BorderLayout.EAST);

        this.add(pnlBottom, BorderLayout.SOUTH);

        for (int i = 0; i < getSt.length; i++) {
            if (i + 1 == getSt.length) {
                listitem[i] = new itemTaskbar(getSt[i][1], getSt[i][0]);
                pnlBottom.add(listitem[i]);
            } else {
                listitem[i] = new itemTaskbar(getSt[i][1], getSt[i][0]);
                pnlCenter.add(listitem[i]);
                if (i != 0) {
                    if (!checkRole(getSt[i][2])) {
                        listitem[i].setVisible(false);
                    }
                }
            }
        }

        listitem[0].setBackground(HowerBackgroundColor);
        listitem[0].setForeground(HowerFontColor);
        listitem[0].setSelected(true);

        for (int i = 0; i < getSt.length; i++) {
            final int index = i;
            listitem[i].addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent evt) {
                    // ✅ Ngăn truy cập sau khi đã đăng xuất
                    if (!SessionManager.isLoggedIn()) {
                        JOptionPane.showMessageDialog(null,
                                "Phiên làm việc đã kết thúc. Vui lòng đăng nhập lại.",
                                "Phiên hết hạn", JOptionPane.WARNING_MESSAGE);
                        main.dispose();
                        //SwingUtilities.invokeLater(() -> new Log_In().setVisible(true));
                        return;
                    }

                    pnlMenuTaskbarMousePress(evt);

                    switch (index) {
                        case 0 -> {
                            trangChu = new TrangChu();
                            main.setPanel(trangChu);
                        }
                        case 1 -> {
                            sanPham = new SanPham(main);
                            main.setPanel(sanPham);
                        }
                        case 2 -> {
                            quanLyThuocTinhSP = new QuanLyThuocTinhSP(main);
                            main.setPanel(quanLyThuocTinhSP);
                        }
                        case 3 -> {
                            quanLyKho = new KhuVucKho(main);
                            main.setPanel(quanLyKho);
                        }
                        case 4 -> {
                            phieuNhap = new PhieuNhap(main, nhanVienDTO);
                            main.setPanel(phieuNhap);
                        }
                        case 5 -> {
                            phieuXuat = new PhieuXuat(main, user);
                            main.setPanel(phieuXuat);
                        }
                        case 6 -> {
                            khachHang = new KhachHang(main);
                            main.setPanel(khachHang);
                        }
                        case 7 -> {
                            nhacungcap = new NhaCungCap(main);
                            main.setPanel(nhacungcap);
                        }
                        case 8 -> {
                            nhanVien = new NhanVien(main);
                            main.setPanel(nhanVien);
                        }
                        case 9 -> {
                            taiKhoan = new TaiKhoan(main);
                            main.setPanel(taiKhoan);
                        }
                        case 10 -> {
                            thongKe = new ThongKe();
                             main.setPanel(thongKe);
                        }
                        case 11 -> {
                            phanQuyen = new PhanQuyen(main);
                            main.setPanel(phanQuyen);
                        }
                        case 12 -> {
                            int input = JOptionPane.showConfirmDialog(null,
                                    "Bạn có chắc chắn muốn đăng xuất?", "Đăng xuất",
                                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);

                            if (input == JOptionPane.OK_OPTION) {
                                if (isDatabaseConnected()) {
                                    SessionManager.logout();
                                    // main.dispose();
                                    // Thu nhỏ cửa sổ chính xuống taskbar
                                    main.setState(JFrame.ICONIFIED);
                                    // Đóng tất cả frame `Log_In` hiện có để tránh trùng
                                    for (Frame f : Frame.getFrames()) {
                                        if (f instanceof Log_In && f.isVisible()) {
                                            f.dispose();
                                        }
                                    }
                                    // Hiển thị cửa sổ đăng nhập mới
                                    SwingUtilities.invokeLater(() -> {
                                        Log_In login = new Log_In();
                                        login.setVisible(true);
                                    });
                                } else {
                                    JOptionPane.showMessageDialog(null,
                                            "Không thể đăng xuất: Mất kết nối với cơ sở dữ liệu!",
                                            "Lỗi kết nối", JOptionPane.ERROR_MESSAGE);
                                }
                            }
                        }
                    }
                }

                private boolean isDatabaseConnected() {
                    Connection conn = null;
                    try {
                        conn = DbConnection.getConnection();
                        return (conn != null && !conn.isClosed());
                    } catch (Exception e) {
                        return false;
                    } finally {
                        DbConnection.closeConnection(conn);
                    }
                }
            });
        }
    }

    public boolean checkRole(String s) {
        boolean check = false;
        for (int i = 0; i < listQuyen.size(); i++) {
            if (listQuyen.get(i).getHanhdong().equals("view")) {
                if (s.equals(listQuyen.get(i).getMachucnang())) {
                    check = true;
                    return check;
                }
            }
        }
        return check;
    }

    public void pnlMenuTaskbarMousePress(MouseEvent evt) {

        for (int i = 0; i < getSt.length; i++) {
            if (evt.getSource() == listitem[i]) {
                listitem[i].setSelected(true); // Sử dụng phương thức setSelected thay vì isSelected = true
                listitem[i].setBackground(HowerBackgroundColor);
                listitem[i].setForeground(HowerFontColor);
            } else {
                listitem[i].setSelected(false); // Sử dụng phương thức setSelected thay vì isSelected = false
                listitem[i].setBackground(DefaultColor);
                listitem[i].setForeground(FontColor);
            }
        }
    }

    public void resetChange() {
        this.nhanVienDTO = new NhanVienDAO().selectById(String.valueOf(nhanVienDTO.getManv()));
    }

    public void in4(JPanel info) {
        JPanel pnlIcon = new JPanel(new FlowLayout());
        pnlIcon.setPreferredSize(new Dimension(60, 0));
        pnlIcon.setOpaque(false);
        info.add(pnlIcon, BorderLayout.WEST);
        JLabel lblIcon = new JLabel();
        lblIcon.setPreferredSize(new Dimension(50, 70));
        if (nhanVienDTO.getGioitinh() == 1) {
            lblIcon.setIcon(new FlatSVGIcon("./Icons/man_50px.svg"));
        } else {
            lblIcon.setIcon(new FlatSVGIcon("./Icons/women_50px.svg"));
        }
        pnlIcon.add(lblIcon);

        JPanel pnlInfo = new JPanel();
        pnlInfo.setOpaque(false);
        pnlInfo.setLayout(new FlowLayout(0, 10, 5));
        pnlInfo.setBorder(new EmptyBorder(15, 0, 0, 0));
        info.add(pnlInfo, BorderLayout.CENTER);

        lblUsername = new JLabel(nhanVienDTO.getHoten());
        lblUsername.putClientProperty("FlatLaf.style", "font: 150% $semibold.font");
        pnlInfo.add(lblUsername);

        lblTenNhomQuyen = new JLabel(nhomQuyenDTO.getTennhomquyen());
        lblTenNhomQuyen.putClientProperty("FlatLaf.style", "font: 120% $light.font");
        lblTenNhomQuyen.setForeground(Color.GRAY);
        pnlInfo.add(lblTenNhomQuyen);

        lblIcon.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent evt) {
                MyAccount ma = new MyAccount(owner, MenuTaskbar.this, "Chỉnh sửa thông tin tài khoản", true);
            }
        });
    }
}
