package View.Panel;

import Model.BUS.SanPhamBUS;
import Model.DAO.HeDieuHanhDAO;
import Model.DAO.KhuVucKhoDAO;
import Model.DAO.ThuongHieuDAO;
import Model.DAO.XuatXuDAO;
import View.Component.IntegratedSearch;
import View.Component.MainFunction;
import View.Main;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import View.Component.PanelBorderRadius;
import View.Component.TableSorter;
import View.Dialog.ChiTietSanPhamDialog;
import View.Dialog.SanPhamDialog;
import Helper.JTableExporter;
import Model.DTO.SanPhamDTO;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

public final class SanPham extends JPanel implements ActionListener {

    PanelBorderRadius main, functionBar, statisticsPanel, titlePanel, searchPanel;
    JPanel pnlBorder1, pnlBorder2, pnlBorder3, pnlBorder4, contentCenter, topPanel;
    JFrame owner = (JFrame) SwingUtilities.getWindowAncestor(this);
    JTable tableSanPham;
    JScrollPane scrollTableSanPham;
    MainFunction mainFunction;
    IntegratedSearch search;
    DefaultTableModel tblModel;
    Main m;
    public SanPhamBUS spBUS = new SanPhamBUS();

    // Thêm các JLabel để hiển thị thống kê
    JLabel lblTongSanPham, lblSapHetHang, lblConHang, lblHetHang;
    JLabel lblTongSanPhamValue, lblSapHetHangValue, lblConHangValue, lblHetHangValue;

    public ArrayList<SanPhamDTO> listSP = spBUS.getAll();

    Color BackgroundColor = new Color(240, 247, 250);

    private void initComponent() {
        this.setBackground(BackgroundColor);
        this.setLayout(new BorderLayout(0, 0));
        this.setOpaque(true);

        initPadding();

        contentCenter = new JPanel();
        contentCenter.setBackground(BackgroundColor);
        contentCenter.setLayout(new BorderLayout(10, 10)); // Tăng khoảng cách giữa các thành phần
        this.add(contentCenter, BorderLayout.CENTER);

        // Tạo thanh tiêu đề và các nút chức năng
        createTitleAndFunctionPanel();
        contentCenter.add(titlePanel, BorderLayout.NORTH);

        // Tạo panel chứa thống kê
        createStatisticsPanel();

        // Tạo panel chứa tìm kiếm và bảng dữ liệu
        createMainContentPanel();

        // Tạo panel chứa statistics và main content với khoảng cách
        JPanel centerWithStats = new JPanel(new BorderLayout(0, 15));
        centerWithStats.setBackground(BackgroundColor);
        centerWithStats.add(statisticsPanel, BorderLayout.NORTH);
        centerWithStats.add(main, BorderLayout.CENTER);

        contentCenter.add(centerWithStats, BorderLayout.CENTER);
    }

    private void createTitleAndFunctionPanel() {
        titlePanel = new PanelBorderRadius();
        titlePanel.setPreferredSize(new Dimension(0, 100));
        titlePanel.setLayout(new BorderLayout());
        titlePanel.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel("Quản lý Sản phẩm");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(51, 51, 51));
        titleLabel.setHorizontalAlignment(SwingConstants.LEFT);

        // Tạo panel chứa các nút chức năng
        functionBar = new PanelBorderRadius();
        functionBar.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        functionBar.setBackground(new Color(240, 247, 250));

        String[] action = {"create", "update", "delete", "detail", "phone", "export"};
        mainFunction = new MainFunction(m.user.getManhomquyen(), "sanpham", action);
        for (String ac : action) {
            mainFunction.btn.get(ac).addActionListener(this);
        }
        functionBar.add(mainFunction);

        titlePanel.add(titleLabel, BorderLayout.WEST);
        titlePanel.add(functionBar, BorderLayout.EAST);
    }

    private void createMainContentPanel() {
        main = new PanelBorderRadius();
        main.setLayout(new BorderLayout(0, 0)); // Tăng khoảng cách giữa search và table
        main.setBorder(new EmptyBorder(0, 0, 0, 0));

        // === Tạo phần tìm kiếm (gộp từ createSearchPanel) ===
//        PanelBorderRadius searchPanel = new PanelBorderRadius();
//        searchPanel.setLayout(new BorderLayout());
//        searchPanel.setBorder(new EmptyBorder(10, 20, 10, 20));
//        searchPanel.setPreferredSize(new Dimension(0, 100));
//        JPanel searchWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
//        searchWrapper.setBackground(Color.WHITE);
        PanelBorderRadius searchPanel = new PanelBorderRadius();
        searchPanel.setPreferredSize(new Dimension(0, 100));
        searchPanel.setLayout(new GridLayout(1, 2, 50, 0));
        searchPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        
        search = new IntegratedSearch(new String[]{"Tất cả", "Theo tên sản phẩm", "Theo mã sản phẩm"});
        search.txtSearchForm.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String keyword = search.txtSearchForm.getText().trim();
                String type = (String) search.cbxChoose.getSelectedItem();  // Lấy loại tìm kiếm

                if (keyword.equals("")) {
                    listSP = spBUS.getAll(); // nếu rỗng thì load lại toàn bộ
                } else {
                    switch (type) {
                        case "Theo mã sản phẩm":
                            listSP = spBUS.searchByMa(keyword);
                            break;
                        case "Theo tên sản phẩm":
                            listSP = spBUS.searchByTen(keyword);
                            break;
                        default: // Tất cả
                            listSP = spBUS.search(keyword); // tìm tất cả theo nhiều tiêu chí
                            break;
                    }
                }

                loadDataTalbe(listSP);
                updateStatistics();
            }
        });

        search.btnReset.addActionListener((ActionEvent e) -> {
            search.txtSearchForm.setText("");
            listSP = spBUS.getAll();
            loadDataTalbe(listSP);
            updateStatistics();
        });

//        searchWrapper.add(search);
//        searchPanel.add(searchWrapper, BorderLayout.WEST);
        searchPanel.add(search);
        JPanel rightPanel = new JPanel(); // có thể để trống
        rightPanel.setOpaque(false); // hoặc setBackground(Color.WHITE);
        searchPanel.add(rightPanel);  // cột 2
        // Thêm searchPanel vào main (vùng NORTH)
        main.add(searchPanel, BorderLayout.NORTH);

        // === Tạo phần bảng dữ liệu ===
        // Khởi tạo table
        tableSanPham = new JTable();
        scrollTableSanPham = new JScrollPane();
        tblModel = new DefaultTableModel();
        String[] header = new String[]{"Mã SP", "Tên sản phẩm", "Số lượng tồn", "Thương hiệu", "Hệ điều hành", "Kích thước màn", "Chip xử lý", "Dung lượng pin", "Xuất xứ", "Khu vực kho"};
        tblModel.setColumnIdentifiers(header);
        tableSanPham.setModel(tblModel);
        scrollTableSanPham.setViewportView(tableSanPham);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        TableColumnModel columnModel = tableSanPham.getColumnModel();
        for (int i = 0; i < 10; i++) {
            if (i != 1) {
                columnModel.getColumn(i).setCellRenderer(centerRenderer);
            }
        }
        tableSanPham.getColumnModel().getColumn(1).setPreferredWidth(180);
        tableSanPham.setFocusable(false);
        tableSanPham.setAutoCreateRowSorter(true);
        TableSorter.configureTableColumnSorter(tableSanPham, 2, TableSorter.INTEGER_COMPARATOR);
        tableSanPham.setDefaultEditor(Object.class, null);
        JPanel tableWrapper = new PanelBorderRadius();
        tableWrapper.setLayout(new BorderLayout());
        tableWrapper.setBorder(new EmptyBorder(0, 20, 0, 20));
        tableWrapper.add(scrollTableSanPham, BorderLayout.CENTER);

        main.add(tableWrapper, BorderLayout.CENTER);
    }

    private void createStatisticsPanel() {
        statisticsPanel = new PanelBorderRadius();
        statisticsPanel.setPreferredSize(new Dimension(0, 120));
        statisticsPanel.setLayout(new GridLayout(1, 4, 15, 0));
        statisticsPanel.setBorder(new EmptyBorder(15, 20, 15, 20));

        // Tạo 4 ô thống kê
        JPanel pnlTongSanPham = createStatisticCard("📦", "Tổng sản phẩm", "0", new Color(52, 152, 219));
        JPanel pnlSapHetHang = createStatisticCard("⚠️", "Sắp hết hàng", "0", new Color(241, 196, 15));
        JPanel pnlConHang = createStatisticCard("✅", "Còn hàng", "0", new Color(39, 174, 96));
        JPanel pnlHetHang = createStatisticCard("❌", "Hết hàng", "0", new Color(231, 76, 60));

        statisticsPanel.add(pnlTongSanPham);
        statisticsPanel.add(pnlSapHetHang);
        statisticsPanel.add(pnlConHang);
        statisticsPanel.add(pnlHetHang);
    }

    private JPanel createStatisticCard(String icon, String title, String value, Color color) {
        JPanel card = new JPanel();
        card.setBackground(Color.WHITE);
        card.setLayout(new BorderLayout(5, 5));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(color, 2),
                new EmptyBorder(10, 10, 10, 10)
        ));

        // Panel cho icon và title
        JPanel topCardPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        topCardPanel.setBackground(Color.WHITE);

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        iconLabel.setForeground(color);

        JLabel titleLabel = new JLabel(" " + title);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        titleLabel.setForeground(Color.GRAY);

        topCardPanel.add(iconLabel);
        topCardPanel.add(titleLabel);

        // Label cho giá trị
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 24));
        valueLabel.setForeground(color);
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Lưu reference để có thể cập nhật sau
        if (title.equals("Tổng sản phẩm")) {
            lblTongSanPhamValue = valueLabel;
        } else if (title.equals("Sắp hết hàng")) {
            lblSapHetHangValue = valueLabel;
        } else if (title.equals("Còn hàng")) {
            lblConHangValue = valueLabel;
        } else if (title.equals("Hết hàng")) {
            lblHetHangValue = valueLabel;
        }

        card.add(topCardPanel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }

    private void updateStatistics() {
        int tongSanPham = listSP.size();
        int sapHetHang = 0;
        int conHang = 0;
        int hetHang = 0;

        for (SanPhamDTO sp : listSP) {
            int soLuong = sp.getSoluongton();
            if (soLuong == 0) {
                hetHang++;
            } else if (soLuong > 0 && soLuong <= 10) { // Sắp hết hàng nếu <= 10
                sapHetHang++;
            } else {
                conHang++;
            }
        }

        // Cập nhật các label
        if (lblTongSanPhamValue != null) {
            lblTongSanPhamValue.setText(String.valueOf(tongSanPham));
        }
        if (lblSapHetHangValue != null) {
            lblSapHetHangValue.setText(String.valueOf(sapHetHang));
        }
        if (lblConHangValue != null) {
            lblConHangValue.setText(String.valueOf(conHang));
        }
        if (lblHetHangValue != null) {
            lblHetHangValue.setText(String.valueOf(hetHang));
        }
    }

    public SanPham(Main m) {
        this.m = m;
        initComponent();
        loadDataTalbe(listSP);
        updateStatistics(); // Cập nhật thống kê lần đầu
    }

    public void loadDataTalbe(ArrayList<SanPhamDTO> result) {
        tblModel.setRowCount(0);
        for (SanPhamDTO sp : result) {
            tblModel.addRow(new Object[]{sp.getMasp(), sp.getTensp(),
                sp.getSoluongton(), ThuongHieuDAO.getInstance().selectById(sp.getThuonghieu() + "").getTenthuonghieu(),
                HeDieuHanhDAO.getInstance().selectById(sp.getHedieuhanh() + "").getTenhdh(),
                sp.getKichthuocman() + " inch",
                sp.getChipxuly(), sp.getDungluongpin() + "mAh",
                XuatXuDAO.getInstance().selectById(sp.getXuatxu() + "").getTenxuatxu(),
                KhuVucKhoDAO.getInstance().selectById(sp.getKhuvuckho() + "").getTenkhuvuc()
            });
        }
        updateStatistics(); // Cập nhật thống kê mỗi khi load dữ liệu
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == mainFunction.btn.get("create")) {
            SanPhamDialog spDialog = new SanPhamDialog(this, owner, "Thêm sản phẩm mới", true, "create");
        } else if (e.getSource() == mainFunction.btn.get("update")) {
            int index = getRowSelected();
            if (index != -1) {
                SanPhamDialog spDialog = new SanPhamDialog(this, owner, "Chỉnh sửa sản phẩm", true, "update", listSP.get(index));
            }
        } else if (e.getSource() == mainFunction.btn.get("delete")) {
            int index = getRowSelected();
            if (index != -1) {
                int input = JOptionPane.showConfirmDialog(null, "Bạn có chắc chắn muốn xóa sản phẩm!", "Xóa sản phẩm", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
                if (input == 0) {
                    spBUS.delete(listSP.get(index));
                    loadDataTalbe(listSP);
                    JOptionPane.showConfirmDialog(null, "Xóa sản phẩm thành công!", "Xóa sản phẩm", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } else if (e.getSource() == mainFunction.btn.get("detail")) {
            int index = getRowSelected();
            if (index != -1) {
                SanPhamDialog spDialog = new SanPhamDialog(this, owner, "Xem chi tiết sản phẩm", true, "view", listSP.get(index));
            }
        } else if (e.getSource() == mainFunction.btn.get("phone")) {
            int index = getRowSelected();
            if (index != -1) {
                ChiTietSanPhamDialog ct = new ChiTietSanPhamDialog(owner, "Tất cả sản phẩm", true, listSP.get(index));
            }
        } else if (e.getSource() == mainFunction.btn.get("export")) {
            try {
                JTableExporter.exportJTableToExcel(tableSanPham);
            } catch (IOException ex) {
                Logger.getLogger(SanPham.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (e.getSource() == mainFunction.btn.get("import")) {
            JOptionPane.showMessageDialog(null, "Chức năng không khả dụng");
        }
    }

    public int getRowSelected() {
        int index = tableSanPham.getSelectedRow();
        if (index == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm");
        }
        return index;
    }

    private void initPadding() {
        pnlBorder1 = new JPanel();
        pnlBorder1.setPreferredSize(new Dimension(0, 10));
        pnlBorder1.setBackground(BackgroundColor);
        this.add(pnlBorder1, BorderLayout.NORTH);

        pnlBorder2 = new JPanel();
        pnlBorder2.setPreferredSize(new Dimension(0, 10));
        pnlBorder2.setBackground(BackgroundColor);
        this.add(pnlBorder2, BorderLayout.SOUTH);

        pnlBorder3 = new JPanel();
        pnlBorder3.setPreferredSize(new Dimension(10, 0));
        pnlBorder3.setBackground(BackgroundColor);
        this.add(pnlBorder3, BorderLayout.EAST);

        pnlBorder4 = new JPanel();
        pnlBorder4.setPreferredSize(new Dimension(10, 0));
        pnlBorder4.setBackground(BackgroundColor);
        this.add(pnlBorder4, BorderLayout.WEST);
    }
}
