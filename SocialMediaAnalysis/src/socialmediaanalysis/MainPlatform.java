package socialmediaanalysis;

import datastructure.Edge_Imp;
import datastructure.Graph_Imp;
import datastructure.Node_Imp;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.ui.swingViewer.ViewPanel;
import org.graphstream.ui.view.View;
import org.graphstream.ui.view.Viewer;
import static socialmediaanalysis.Clicks.old_edge;
import socialmediaanalysisalgorithms.BetweennessCentrality;
import socialmediaanalysisalgorithms.ClosenessCentrality;
import socialmediaanalysisalgorithms.DegreeCentrality;

public class MainPlatform extends javax.swing.JFrame {
    
    public static Graph graph;
    public static Graph_Imp implemented_graph;
    public static DegreeCentrality degreeCentralityGraph;
    public static ClosenessCentrality closenessCentralityGraph;
    public static BetweennessCentrality betweennessCentralityGraph;
    
    public static Viewer viewer;
    
    private JFrame frame;
    private View view;
    private Clicks ct;
    
    public static boolean request_change;
    public static String mode;
    public static boolean ON_OFF;
    public static boolean algorithm_on;
    public static int last_id;
    public static String selected_edge;
    public String alogrethm;

    /**
     * Creates new form MainPlatform
     */
    public MainPlatform() {
        initComponents();
        initializeComboBox();
        initializeComboBox2();
        InitializeFileChooser();
        jPanel1.setVisible(false);
        request_change = false;
        algorithm_on = false;
        ON_OFF = false;
        last_id = -1;
        mode = "none";
    }
    
    private void InitializeFileChooser() {
        jFileChooser1.setDialogTitle("Open Source File");
        
        jFileChooser1.setAcceptAllFileFilterUsed(false);
        FileNameExtensionFilter restrict = new FileNameExtensionFilter(".txt files", "txt");
        jFileChooser1.addChoosableFileFilter(restrict);
        
        jFileChooser2.setDialogTitle("Save Screenshot");
        
        jFileChooser2.setAcceptAllFileFilterUsed(false);
        FileNameExtensionFilter restrict2 = new FileNameExtensionFilter(".png files", "png");
        jFileChooser2.addChoosableFileFilter(restrict2);
        
    }
    
    private void initializeComboBox() {
        jComboBox1.removeAllItems();
        jComboBox1.addItem("Free Move");
        jComboBox1.addItem("Add Edge");
        jComboBox1.addItem("Add Node");
        jComboBox1.addItem("Node Edges");
        jComboBox1.addItem("Remove Node");
        jComboBox1.addItem("Remove Edge");
        jComboBox1.addItem("Change Weight");
        
    }
    
    private void initializeComboBox2() {
        jComboBox2.removeAllItems();
        jComboBox2.addItem("None");
        jComboBox2.addItem("Degree Centrality");
        jComboBox2.addItem("Betweenness Centrality");
        jComboBox2.addItem("Closeness Centrality");
        
    }
    
    public void darw_node_id__edge_weight() {
        
        for (org.graphstream.graph.Node node : graph) {
            node.addAttribute("ui.label", node.getId());
            last_id++;
        }
        
        for (Edge edge : graph.getEachEdge()) {
            edge.addAttribute("ui.label", implemented_graph.getNode(Integer.valueOf(edge.getNode0().getId())).getChildren_byID(Integer.valueOf(edge.getNode1().getId())).getWeight());
        }
    }
    
    public void clear_the_table() {
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        int rowCount = model.getRowCount();
        for (int i = rowCount - 1; i >= 0; i--) {
            model.removeRow(i);
        }
    }
    
    public void add_in_table_new_row(int _id, double cent) {
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.addRow(new String[]{Integer.toString(_id), Double.toString(cent)});
    }
    
    private void loadFromFile() throws FileNotFoundException {
        int r = jFileChooser1.showOpenDialog(null);
        File file = null;
        if (r == JFileChooser.APPROVE_OPTION) {
            file = jFileChooser1.getSelectedFile();
            
            Scanner scanner = new Scanner(file);
            int no_nodes = 0;
            int no_edges = 0;
            
            if (scanner.hasNextLine()) {
                no_nodes = scanner.nextInt();
                no_edges = scanner.nextInt();
            }
            
            implemented_graph = new Graph_Imp(no_nodes);
            graph = new MultiGraph("Graph Visualization");
            graphVisualization();
            set_styleSheet();
            ct = new Clicks();
            ct.start();
            
            for (int i = 0; i < no_nodes; i++) {
                graph.addNode(Integer.toString(i));
            }
            
            int src;
            int dest;
            double wt;
            Edge_Imp edg;
            
            for (int i = 0; i < no_edges; i++) {
                if (scanner.hasNextLine()) {
                    src = scanner.nextInt();
                    dest = scanner.nextInt();
                    wt = scanner.nextDouble();
                    
                    edg = new Edge_Imp(implemented_graph.getNode(src), wt);
                    implemented_graph.getNode(dest).addChild(edg);
                    edg = new Edge_Imp(implemented_graph.getNode(dest), wt);
                    implemented_graph.getNode(src).addChild(edg);
                    
                    graph.addEdge(Integer.toString(i), Integer.toString(src), Integer.toString(dest));
                }
            }
            scanner.close();
            darw_node_id__edge_weight();
            ON_OFF = true;
            
        }
    }

    //======================start the gui=====================================//
    public void graphVisualization() {
        System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");//gs.ui.renderer
        frame = new JFrame();
        frame.setResizable(true);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        JPanel panel = new JPanel(new GridLayout()) {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(640, 480);
            }
        };
        
        viewer = new Viewer(graph, Viewer.ThreadingModel.GRAPH_IN_GUI_THREAD);
        ViewPanel viewPanel = viewer.addDefaultView(false);
        panel.add(viewPanel);
        frame.add(panel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        view = viewer.getDefaultView();
        view.getCamera().setAutoFitView(true);
        viewer.enableAutoLayout();
    }

    //=======================style sheet======================================//
    public void set_styleSheet() {
        //===================style sheet graph================================//
        graph.addAttribute("ui.quality");
        graph.addAttribute("ui.antialias");
        graph.addAttribute("ui.stylesheet", "graph {  fill-color: black;}");
        //=================style sheet node ==================================//
        graph.addAttribute("ui.stylesheet", "node { "
                + "text-size:17px;"
                + "size-mode: dyn-size;"
                + "fill-mode: dyn-plain;"
                + "size:24px;"
                + "fill-color: #CB00F3;"
                + "text-mode:normal;"
                + "text-alignment:center; "
                + "text-color:#2B1C1A;"
                + "shape:circle;"
                + " }");
        //=====================style sheet edge ==============================//
        graph.addAttribute("ui.stylesheet", "edge { "
                + "shape:cubic-curve;"
                + "size:5px; "
                + "fill-color: #0867A0;"
                + "text-mode:normal;"
                + "text-visibility-mode:hidden;"
                + "text-size:17px;"
                + "text-color:gold;"
                + "text-alignment:along;"
                + "}");
        //=============================style sheet marked=====================//
        graph.addAttribute("ui.stylesheet", " node.marked {fill-color: green;}");
        graph.addAttribute("ui.stylesheet", " edge.marked {fill-color:green;}");
    }
    
    public static void color_generator(double input, Node node) {
        
        double reminder;
        double size;
        if (input >= 0 && input < 10) {
            reminder = input;//0 1 2 3 4 5 6 7 8 9
            size = reminder * 4 + 20;
            
            node.changeAttribute("ui.color", Color.decode("#ffff00"));
            node.changeAttribute("ui.size", size);
            //#ffff00
        } else if ((input >= 10 && input < 20)) {
            reminder = (input - 10);// 0 1 2 3 4 5 6 7 8 9 
            size = reminder * 4 + 20;
            
            node.changeAttribute("ui.color", Color.decode("#ffae42"));
            node.changeAttribute("ui.size", size);
            //#ffae42 

        } else if ((input >= 20 && input < 30)) {
            reminder = (input - 20);// 0 1 2 3 4 5 6 7 8 9 
            size = reminder * 4 + 20;
            node.changeAttribute("ui.color", Color.decode("#FF7200"));
            node.changeAttribute("ui.size", size);
            //#FFA500

        } else if ((input >= 30 && input < 40)) {
            reminder = (input - 30);// 0 1 2 3 4 5 6 7 8 9 
            size = reminder * 4 + 20;
            node.changeAttribute("ui.color", Color.decode("#ff4500"));
            node.changeAttribute("ui.size", size);
            //#ff4500 

        } else if ((input >= 40 && input < 50)) {
            reminder = (input - 40);// 0 1 2 3 4 5 6 7 8 9 
            size = reminder * 4 + 20;
            node.changeAttribute("ui.color", Color.decode("#ff0000"));
            node.changeAttribute("ui.size", size);
            //#ff0000  

        } else if ((input >= 50 && input < 60)) {
            reminder = (input - 50);// 0 1 2 3 4 5 6 7 8 9 
            size = reminder * 4 + 20;
            node.changeAttribute("ui.color", Color.decode("#c71585"));
            node.changeAttribute("ui.size", size);
            //#c71585   

        } else if ((input >= 60 && input < 70)) {
            reminder = (input - 60);// 0 1 2 3 4 5 6 7 8 9 
            size = reminder * 4 + 20;
            node.changeAttribute("ui.color", Color.decode("#800080"));
            node.changeAttribute("ui.size", size);
            //#800080   

        } else if ((input >= 70 && input < 80)) {
            reminder = (input - 70);// 0 1 2 3 4 5 6 7 8 9 
            size = reminder * 4 + 20;
            node.changeAttribute("ui.color", Color.decode("#8a2be2"));
            node.changeAttribute("ui.size", size);
            //#8a2be2    

        } else if ((input >= 80 && input < 90)) {
            reminder = (input - 80);// 0 1 2 3 4 5 6 7 8 9 
            size = reminder * 4 + 20;
            node.changeAttribute("ui.color", Color.decode("#0000ff"));
            node.changeAttribute("ui.size", size);
            //#0000ff     

        } else if ((input >= 90 && input <= 100)) {
            reminder = (input - 90);// 0 1 2 3 4 5 6 7 8 9 
            size = reminder * 4 + 20;
            node.changeAttribute("ui.color", Color.decode("#0d98ba"));
            node.changeAttribute("ui.size", size);
            //#0d98ba      
        }
    }
    
    public static void initialize() {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                    
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainPlatform.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
            
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainPlatform.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
            
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainPlatform.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
            
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainPlatform.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainPlatform().setVisible(true);
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jFileChooser1 = new javax.swing.JFileChooser();
        jFileChooser2 = new javax.swing.JFileChooser();
        jPanel2 = new javax.swing.JPanel();
        jButton3 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jTextField1 = new javax.swing.JTextField();
        jComboBox1 = new javax.swing.JComboBox<>();
        jToggleButton1 = new javax.swing.JToggleButton();
        jLabel3 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox<>();
        jButton12 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jToggleButton2 = new javax.swing.JToggleButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel6 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(0, 255, 255));
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setForeground(new java.awt.Color(255, 51, 102));
        setResizable(false);

        jPanel2.setBackground(new java.awt.Color(0, 0, 51));

        jButton3.setBackground(new java.awt.Color(38, 35, 114));
        jButton3.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText("-");
        jButton3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton3.setName(""); // NOI18N
        jButton3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ZoomOut(evt);
            }
        });

        jButton7.setBackground(new java.awt.Color(38, 35, 114));
        jButton7.setForeground(new java.awt.Color(255, 255, 255));
        jButton7.setText("[ ]");
        jButton7.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                zoomFit(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(38, 35, 114));
        jButton2.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("+");
        jButton2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton2.setName(""); // NOI18N
        jButton2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ZoomIn(evt);
            }
        });

        jButton1.setBackground(new java.awt.Color(38, 35, 114));
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Close Graph ");
        jButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton1.setName(""); // NOI18N
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                graphFrameClose(evt);
            }
        });

        jButton8.setBackground(new java.awt.Color(38, 35, 114));
        jButton8.setForeground(new java.awt.Color(255, 255, 255));
        jButton8.setText("Load graph from file");
        jButton8.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                loadGraphFromFile(evt);
            }
        });

        jButton6.setBackground(new java.awt.Color(38, 35, 114));
        jButton6.setForeground(new java.awt.Color(255, 255, 255));
        jButton6.setText("Screenshot");
        jButton6.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton6.setMaximumSize(new java.awt.Dimension(144, 32));
        jButton6.setMinimumSize(new java.awt.Dimension(144, 32));
        jButton6.setPreferredSize(new java.awt.Dimension(144, 32));
        jButton6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                screenshot(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(0, 0, 51));

        jLabel1.setBackground(new java.awt.Color(255, 255, 255));
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Old Weight");

        jLabel2.setBackground(new java.awt.Color(255, 255, 255));
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("New weight");

        jTextField2.setBackground(new java.awt.Color(38, 35, 114));
        jTextField2.setForeground(new java.awt.Color(255, 255, 255));
        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });

        jTextField1.setEditable(false);
        jTextField1.setBackground(new java.awt.Color(38, 35, 114));
        jTextField1.setForeground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(jLabel1)
                        .addGap(32, 32, 32)
                        .addComponent(jLabel2))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jComboBox1.setBackground(new java.awt.Color(38, 35, 114));
        jComboBox1.setForeground(new java.awt.Color(255, 255, 255));
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jToggleButton1.setBackground(new java.awt.Color(38, 35, 114));
        jToggleButton1.setForeground(new java.awt.Color(255, 255, 255));
        jToggleButton1.setText("View Weights");
        jToggleButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jToggleButton1.setMaximumSize(new java.awt.Dimension(144, 32));
        jToggleButton1.setMinimumSize(new java.awt.Dimension(144, 32));
        jToggleButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                viewWeights(evt);
            }
        });

        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Options");

        jComboBox2.setBackground(new java.awt.Color(38, 35, 114));
        jComboBox2.setForeground(new java.awt.Color(255, 255, 255));
        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        jButton12.setBackground(new java.awt.Color(38, 35, 114));
        jButton12.setForeground(new java.awt.Color(255, 255, 255));
        jButton12.setText("Draw");
        jButton12.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Start(evt);
            }
        });

        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Algorithm");

        jToggleButton2.setBackground(new java.awt.Color(38, 35, 114));
        jToggleButton2.setForeground(new java.awt.Color(255, 255, 255));
        jToggleButton2.setText("Manual Layout");
        jToggleButton2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jToggleButton2.setMaximumSize(new java.awt.Dimension(144, 32));
        jToggleButton2.setMinimumSize(new java.awt.Dimension(144, 32));
        jToggleButton2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                change_layout(evt);
            }
        });

        jTable1.setBackground(new java.awt.Color(0, 0, 102));
        jTable1.setBorder(new javax.swing.border.MatteBorder(null));
        jTable1.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        jTable1.setForeground(new java.awt.Color(255, 153, 0));
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Node ID", "Centrality"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setGridColor(new java.awt.Color(102, 204, 0));
        jTable1.setIntercellSpacing(new java.awt.Dimension(2, 1));
        jTable1.setSelectionBackground(new java.awt.Color(204, 0, 0));
        jTable1.setSelectionForeground(new java.awt.Color(255, 255, 0));
        jScrollPane1.setViewportView(jTable1);

        jLabel6.setBackground(new java.awt.Color(255, 255, 255));
        jLabel6.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Algorethm Centrality ");

        jLabel4.setBackground(new java.awt.Color(200, 255, 123));
        jLabel4.setFont(new java.awt.Font("Dialog", 1, 48)); // NOI18N
        jLabel4.setForeground(java.awt.Color.darkGray);
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/socialmediaanalysis/poster,840x830,f8f8f8-pad,750x1000,f8f8f8.jpg"))); // NOI18N

        jPanel3.setOpaque(false);

        jLabel7.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Graoh Color Code");

        jLabel10.setBackground(new java.awt.Color(255, 255, 0));
        jLabel10.setMaximumSize(new java.awt.Dimension(20, 5));
        jLabel10.setMinimumSize(new java.awt.Dimension(20, 5));
        jLabel10.setOpaque(true);
        jLabel10.setPreferredSize(new java.awt.Dimension(20, 5));

        jLabel9.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("10 - 20");

        jLabel12.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("20 - 30");

        jLabel13.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("30 - 40");

        jLabel14.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("40 - 50");

        jLabel11.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("70 - 80");

        jLabel15.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("50 - 60");

        jLabel16.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setText("60 - 70");

        jLabel17.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setText("80 - 90");

        jLabel18.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(255, 255, 255));
        jLabel18.setText("0 - 10");

        jLabel8.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("90 - 100");

        jLabel19.setBackground(new java.awt.Color(255, 174, 66));
        jLabel19.setOpaque(true);

        jLabel20.setBackground(new java.awt.Color(255, 114, 0));
        jLabel20.setOpaque(true);

        jLabel21.setBackground(new java.awt.Color(255, 69, 0));
        jLabel21.setOpaque(true);

        jLabel22.setBackground(new java.awt.Color(255, 0, 0));
        jLabel22.setOpaque(true);

        jLabel23.setBackground(new java.awt.Color(199, 21, 133));
        jLabel23.setOpaque(true);

        jLabel25.setBackground(new java.awt.Color(128, 0, 128));
        jLabel25.setOpaque(true);

        jLabel26.setBackground(new java.awt.Color(138, 43, 226));
        jLabel26.setOpaque(true);

        jLabel27.setBackground(new java.awt.Color(0, 0, 255));
        jLabel27.setOpaque(true);

        jLabel28.setBackground(new java.awt.Color(13, 152, 186));
        jLabel28.setOpaque(true);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel14, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel15, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jLabel7))
                .addContainerGap(23, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(203, 203, 203)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jToggleButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(27, 27, 27)
                                .addComponent(jLabel3))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(23, 23, 23)
                                .addComponent(jLabel5))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel4))
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jToggleButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 348, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 364, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton7)
                            .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(52, 52, 52)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 51, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jToggleButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jToggleButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(5, 5, 5)
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(12, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addGap(12, 12, 12))))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void change_layout(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_change_layout
        if (ON_OFF) {
            if (jToggleButton2.isSelected()) {
                request_change = true;
                viewer.disableAutoLayout();
                request_change = false;
            } else {
                request_change = true;
                viewer.enableAutoLayout();
                request_change = false;
            }
        }
    }//GEN-LAST:event_change_layout

    private void Start(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Start
        alogrethm = (String) jComboBox2.getSelectedItem();
        
        if (ON_OFF) {
            if (alogrethm == "Degree Centrality") {
                clear_the_table();
                degreeCentralityGraph = new DegreeCentrality(implemented_graph);
                degreeCentralityGraph.calculation();
                double maxDegree = degreeCentralityGraph.getMaxCentrality();
                for (int i = 0; i < implemented_graph.getNoVertices(); i++) {
                    double degree = (degreeCentralityGraph.getNode(i).getCentrality() / (double) maxDegree) * 100;
                    color_generator(degree, graph.getNode(Integer.toString(degreeCentralityGraph.getNode(i).getID())));
                    add_in_table_new_row(degreeCentralityGraph.getNode(i).getID(), degreeCentralityGraph.getNode(i).getCentrality());
                }
            }//degree
            else if (alogrethm == "Betweenness Centrality") {
                clear_the_table();
                betweennessCentralityGraph = new BetweennessCentrality(implemented_graph);
                betweennessCentralityGraph.calculation();
                double maxDegree = betweennessCentralityGraph.getMaxCentrality();
                for (int i = 0; i < implemented_graph.getNoVertices(); i++) {
                    double degree = (betweennessCentralityGraph.getNode(i).getCentrality() / (double) maxDegree) * 100;
                    color_generator(degree, graph.getNode(Integer.toString(betweennessCentralityGraph.getNode(i).getID())));
                    add_in_table_new_row(betweennessCentralityGraph.getNode(i).getID(), betweennessCentralityGraph.getNode(i).getCentrality());
                }
                
            } else if (alogrethm == "Closeness Centrality") {
                clear_the_table();
                closenessCentralityGraph = new ClosenessCentrality(implemented_graph);
                closenessCentralityGraph.calculation();
                double maxDegree = closenessCentralityGraph.getMaxCentrality();
                for (int i = 0; i < implemented_graph.getNoVertices(); i++) {
                    double degree = (closenessCentralityGraph.getNode(i).getCentrality() / (double) maxDegree) * 100;
                    color_generator(degree, graph.getNode(Integer.toString(closenessCentralityGraph.getNode(i).getID())));
                    add_in_table_new_row(closenessCentralityGraph.getNode(i).getID(), closenessCentralityGraph.getNode(i).getCentrality());
                }
            } else if (alogrethm == "None") {
                set_styleSheet();
                view.getCamera().resetView();
                for (org.graphstream.graph.Node node : graph) {
                    node.addAttribute("ui.size", 24);
                    node.removeAttribute("ui.color");
                }
                clear_the_table();
                algorithm_on = false;
            }
            
        }
    }//GEN-LAST:event_Start

    private void viewWeights(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_viewWeights
        if (ON_OFF) {
            if (jToggleButton1.isSelected()) {
                graph.setAttribute("ui.stylesheet", "edge { "
                        + "text-visibility-mode:normal;"
                        + "}");
            } else {
                graph.setAttribute("ui.stylesheet", "edge { "
                        + "text-visibility-mode:hidden;"
                        + "}");
            }
        }
    }//GEN-LAST:event_viewWeights

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        mode = (String) jComboBox1.getSelectedItem();
        if (mode == "Change Weight") {
            jPanel1.setVisible(true);
        } else {
            jPanel1.setVisible(false);
        }
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        // change weight
        double new_weight;
        new_weight = Double.parseDouble(jTextField2.getText());
        graph.getEdge(selected_edge).setAttribute("ui.label", new_weight);//request change in the main graph
        graph.getEdge(selected_edge).removeAttribute("ui.class");
        //========================change weight===============================//

        String src = old_edge.getNode0().getId();
        String des = old_edge.getNode1().getId();
        int src_index = Integer.parseInt(src);
        Node_Imp _node = new Node_Imp(false);
        _node.setID(src_index);
        src_index = implemented_graph.get_node_index(_node);
        
        for (int i = 0; i < implemented_graph.getNode(src_index).getNoChildren(); i++) {
            int id = implemented_graph.getNode(src_index).getChildren_byIndex(i).getChild().getID();
            if (id == Integer.parseInt(des)) {
                implemented_graph.getNode(src_index).getChildren_byIndex(i).setWeight(new_weight);
                break;
            }
        }
        des = old_edge.getNode0().getId();
        src = old_edge.getNode1().getId();
        src_index = Integer.parseInt(src);
        _node = new Node_Imp(false);
        _node.setID(src_index);
        src_index = implemented_graph.get_node_index(_node);
        
        for (int i = 0; i < implemented_graph.getNode(src_index).getNoChildren(); i++) {
            int id = implemented_graph.getNode(src_index).getChildren_byIndex(i).getChild().getID();
            if (id == Integer.parseInt(des)) {
                implemented_graph.getNode(src_index).getChildren_byIndex(i).setWeight(new_weight);
                break;
            }
        }
        
        jTextField1.setText("");
        jTextField2.setText("");
        //====================================================================//
    }//GEN-LAST:event_jTextField2ActionPerformed

    private void screenshot(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_screenshot
        if (ON_OFF) {
            int r = jFileChooser2.showSaveDialog(null);
            if (r == JFileChooser.APPROVE_OPTION) {
                String path = jFileChooser2.getSelectedFile().getAbsolutePath();
                path += ".png";
                graph.addAttribute("ui.screenshot", path);
            }
        }
    }//GEN-LAST:event_screenshot

    private void loadGraphFromFile(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_loadGraphFromFile
        try {
            loadFromFile();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MainPlatform.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_loadGraphFromFile

    private void graphFrameClose(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_graphFrameClose
        if (ON_OFF) {
            frame.dispose();
            mode = "close";
            viewer.close();
            clear_the_table();
            ON_OFF = false;
        }
    }//GEN-LAST:event_graphFrameClose

    private void ZoomIn(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ZoomIn
        if (ON_OFF) {
            view.getCamera().setViewPercent(view.getCamera().getViewPercent() - 0.1);
        }
    }//GEN-LAST:event_ZoomIn

    private void zoomFit(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_zoomFit
        if (ON_OFF) {
            view.getCamera().setViewPercent(1);
        }
    }//GEN-LAST:event_zoomFit

    private void ZoomOut(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ZoomOut
        if (ON_OFF) {
            view.getCamera().setViewPercent(view.getCamera().getViewPercent() + 0.1);
        }
    }//GEN-LAST:event_ZoomOut


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JFileChooser jFileChooser1;
    private javax.swing.JFileChooser jFileChooser2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    public static javax.swing.JTextField jTextField1;
    public static javax.swing.JTextField jTextField2;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JToggleButton jToggleButton2;
    // End of variables declaration//GEN-END:variables
}
