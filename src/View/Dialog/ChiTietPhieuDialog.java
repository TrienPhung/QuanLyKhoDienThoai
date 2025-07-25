package View.Dialog;

import Model.BUS.ChiTietSanPhamBUS;
import Model.BUS.DungLuongRamBUS;
import Model.BUS.DungLuongRomBUS;
import Model.BUS.MauSacBUS;
import Model.BUS.PhienBanSanPhamBUS;
import Model.BUS.PhieuNhapBUS;
import Model.BUS.PhieuXuatBUS;
import Model.BUS.SanPhamBUS;
import Model.DAO.DungLuongRamDAO;
import Model.DAO.DungLuongRomDAO;
import Model.DAO.KhachHangDAO;
import Model.DAO.MauSacDAO;
import Model.DAO.NhaCungCapDAO;
import Model.DAO.NhanVienDAO;
import Model.DAO.SanPhamDAO;
import Model.DTO.ChiTietPhieuDTO;
import Model.DTO.ChiTietSanPhamDTO;
import Model.DTO.PhienBanSanPhamDTO;
import Model.DTO.PhieuNhapDTO;
import Model.DTO.PhieuXuatDTO;
import View.Component.ButtonCustom;
import View.Component.HeaderTitle;
import View.Component.InputForm;
import Helper.Formater;
import Helper.writePDF;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public final class ChiTietPhieuDialog extends JDialog implements ActionListener {

    HeaderTitle titlePage;
    JPanel pnmain, pnmain_top, pnmain_bottom, pnmain_bottom_right, pnmain_bottom_left, pnmain_btn;
    InputForm txtMaPhieu, txtNhanVien, txtNhaCungCap, txtThoiGian;
    DefaultTableModel tblModel, tblModelImei;
    JTable table, tblImei;
    JScrollPane scrollTable, scrollTableImei;

    PhieuNhapDTO phieunhap;
    PhieuXuatDTO phieuxuat;
    PhienBanSanPhamBUS phienbanBus = new PhienBanSanPhamBUS();
    ChiTietSanPhamBUS ctspBus = new ChiTietSanPhamBUS();
    PhieuNhapBUS phieunhapBus;
    PhieuXuatBUS phieuxuatBus;

    ButtonCustom btnPdf, btnHuyBo;

    ArrayList<ChiTietPhieuDTO> chitietphieu;

    HashMap<Integer, ArrayList<ChiTietSanPhamDTO>> chitietsanpham = new HashMap<>();

    public ChiTietPhieuDialog(JFrame owner, String title, boolean modal, PhieuNhapDTO phieunhapDTO) {
        super(owner, title, modal);
        this.phieunhap = phieunhapDTO;
        phieunhapBus = new PhieuNhapBUS();
        chitietphieu = phieunhapBus.getChiTietPhieu_Type(phieunhapDTO.getMaphieu());
        chitietsanpham = ctspBus.getChiTietSanPhamFromMaPN(phieunhapDTO.getMaphieu());
        initComponent(title);
        initPhieuNhap();
        loadDataTableChiTietPhieu(chitietphieu);
        this.setVisible(true);
    }

    public ChiTietPhieuDialog(JFrame owner, String title, boolean modal, PhieuXuatDTO phieuxuatDTO) {
        super(owner, title, modal);
        this.phieuxuat = phieuxuatDTO;
        phieuxuatBus = new PhieuXuatBUS();
        chitietphieu = phieuxuatBus.selectCTP(phieuxuatDTO.getMaphieu());
        chitietsanpham = ctspBus.getChiTietSanPhamFromMaPX(phieuxuatDTO.getMaphieu());
        initComponent(title);
        initPhieuXuat();
        loadDataTableChiTietPhieu(chitietphieu);
        this.setVisible(true);
    }

    public void initPhieuNhap() {
        txtMaPhieu.setText("PN" + Integer.toString(this.phieunhap.getMaphieu()));
        txtNhaCungCap.setText(NhaCungCapDAO.getInstance().selectById(phieunhap.getManhacungcap() + "").getTenncc());
        txtNhanVien.setText(NhanVienDAO.getInstance().selectById(phieunhap.getManguoitao() + "").getHoten());
        txtThoiGian.setText(Formater.FormatTime(phieunhap.getThoigiantao()));
    }

    public void initPhieuXuat() {
        txtMaPhieu.setText("PX" + Integer.toString(this.phieuxuat.getMaphieu()));
        txtNhaCungCap.setTitle("Khách hàng");
        txtNhaCungCap.setText(KhachHangDAO.getInstance().selectById(phieuxuat.getMakh() + "").getHoten());
        txtNhanVien.setText(NhanVienDAO.getInstance().selectById(phieuxuat.getManguoitao() + "").getHoten());
        txtThoiGian.setText(Formater.FormatTime(phieuxuat.getThoigiantao()));
    }

    public void loadDataTableChiTietPhieu(ArrayList<ChiTietPhieuDTO> ctPhieu) {
        tblModel.setRowCount(0);
        int size = ctPhieu.size();
        for (int i = 0; i < size; i++) {
            PhienBanSanPhamDTO pb = phienbanBus.getByMaPhienBan(ctPhieu.get(i).getMaphienbansp());
            tblModel.addRow(new Object[]{
                i + 1, pb.getMasp(), SanPhamDAO.getInstance().selectById(pb.getMasp()+"").getTensp(), 
                DungLuongRamDAO.getInstance().selectById(pb.getRam()+"").getDungluongram() + "GB",
                DungLuongRomDAO.getInstance().selectById(pb.getRom()+"").getDungluongrom() + "GB", 
                MauSacDAO.getInstance().selectById(pb.getMausac()+"").getTenmau(),
                Formater.FormatVND(ctPhieu.get(i).getDongia()), ctPhieu.get(i).getSoluong()
            });
        }
    }

    public void loadDataTableImei(ArrayList<ChiTietSanPhamDTO> dssp) {
        tblModelImei.setRowCount(0);
        int size = dssp.size();
        for (int i = 0; i < size; i++) {
            tblModelImei.addRow(new Object[]{
                i + 1, dssp.get(i).getImei()
            });
        }
    }

    public void initComponent(String title) {
        this.setSize(new Dimension(1100, 500));
        this.setLayout(new BorderLayout(0, 0));
        titlePage = new HeaderTitle(title.toUpperCase());

        pnmain = new JPanel(new BorderLayout());

        pnmain_top = new JPanel(new GridLayout(1, 4));
        txtMaPhieu = new InputForm("Mã phiếu");
        txtNhanVien = new InputForm("Nhân viên nhập");
        txtNhaCungCap = new InputForm("Nhà cung cấp");
        txtThoiGian = new InputForm("Thời gian tạo");

        txtMaPhieu.setEditable(false);
        txtNhanVien.setEditable(false);
        txtNhaCungCap.setEditable(false);
        txtThoiGian.setEditable(false);

        pnmain_top.add(txtMaPhieu);
        pnmain_top.add(txtNhanVien);
        pnmain_top.add(txtNhaCungCap);
        pnmain_top.add(txtThoiGian);

        pnmain_bottom = new JPanel(new BorderLayout(5, 5));
        pnmain_bottom.setBorder(new EmptyBorder(5, 5, 5, 5));
        pnmain_bottom.setBackground(Color.WHITE);

        pnmain_bottom_left = new JPanel(new GridLayout(1, 1));
        table = new JTable();
        scrollTable = new JScrollPane();
        tblModel = new DefaultTableModel();
        String[] header = new String[]{"STT", "Mã SP", "Tên SP", "RAM", "ROM", "Màu sắc", "Đơn giá", "Số lượng"};
        tblModel.setColumnIdentifiers(header);
        table.setModel(tblModel);
        table.setFocusable(false);
        scrollTable.setViewportView(table);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.setDefaultRenderer(Object.class, centerRenderer);
        table.getColumnModel().getColumn(2).setPreferredWidth(200);
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int index = table.getSelectedRow();
                if (index != -1) {
                    loadDataTableImei(chitietsanpham.get(chitietphieu.get(index).getMaphienbansp()));
                }
            }
        });
        pnmain_bottom_left.add(scrollTable);

        pnmain_bottom_right = new JPanel(new GridLayout(1, 1));
        pnmain_bottom_right.setPreferredSize(new Dimension(200, 10));
        tblImei = new JTable();
        scrollTableImei = new JScrollPane();
        tblModelImei = new DefaultTableModel();
        tblModelImei.setColumnIdentifiers(new String[]{"STT", "Mã Imei"});
        tblImei.setModel(tblModelImei);
        tblImei.setFocusable(false);
        tblImei.setDefaultRenderer(Object.class, centerRenderer);
        tblImei.getColumnModel().getColumn(1).setPreferredWidth(170);
        scrollTableImei.setViewportView(tblImei);
        pnmain_bottom_right.add(scrollTableImei);

        pnmain_bottom.add(pnmain_bottom_left, BorderLayout.CENTER);
        pnmain_bottom.add(pnmain_bottom_right, BorderLayout.EAST);

        pnmain_btn = new JPanel(new FlowLayout());
        pnmain_btn.setBorder(new EmptyBorder(10, 0, 10, 0));
        pnmain_btn.setBackground(Color.white);
        btnPdf = new ButtonCustom("Xuất file PDF", "success", 14);
        btnHuyBo = new ButtonCustom("Huỷ bỏ", "danger", 14);
        btnPdf.addActionListener(this);
        btnHuyBo.addActionListener(this);
        pnmain_btn.add(btnPdf);
        pnmain_btn.add(btnHuyBo);

        pnmain.add(pnmain_top, BorderLayout.NORTH);
        pnmain.add(pnmain_bottom, BorderLayout.CENTER);
        pnmain.add(pnmain_btn, BorderLayout.SOUTH);

        this.add(titlePage, BorderLayout.NORTH);
        this.add(pnmain, BorderLayout.CENTER);
        this.setLocationRelativeTo(null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == btnHuyBo) {
            dispose();
        }
        if (source == btnPdf) {
            writePDF w = new writePDF();
            if (this.phieuxuat != null) {
                w.writePX(phieuxuat.getMaphieu());
            }
            if (this.phieunhap != null) {
                w.writePN(phieunhap.getMaphieu());
            }
        }
    }
}
