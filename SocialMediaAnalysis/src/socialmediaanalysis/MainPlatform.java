package socialmediaanalysis;

import datastructure.Edge_Imp;
import datastructure.Graph_Imp;
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
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.ui.swingViewer.ViewPanel;
import org.graphstream.ui.view.View;
import org.graphstream.ui.view.Viewer;

public class MainPlatform extends javax.swing.JFrame {

    public static Graph graph;
    public static Graph_Imp implemented_graph;
    public static Viewer viewer;

    private JFrame frame;
    private View view;
    private Clicks ct;

    public static boolean request_change;
    public static String mode;
    ;
    public static boolean algorithm_on = false;
    public static int last_id;
    public static String selected_edge;

    /**
     * Creates new form MainPlatform
     */
    public MainPlatform() {
        initComponents();
        initializeComboBox();
        request_change = false;
        mode = "none";

    }

    public void darw_node_id__edge_weight() {
        for (org.graphstream.graph.Node node : graph) {
            node.addAttribute("ui.label", node.getId());
        }
        for (Edge edge : graph.getEachEdge()) {
            edge.addAttribute("ui.label", implemented_graph.getNode(Integer.valueOf(edge.getNode0().getId())).getChildren_byID(Integer.valueOf(edge.getNode1().getId())).getWeight());
        }
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
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jToggleButton1 = new javax.swing.JToggleButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jComboBox1 = new javax.swing.JComboBox<>();
        jButton8 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jButton1.setText("Close Graph Visulaization");
        jButton1.setName(""); // NOI18N
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                graphFrameClose(evt);
            }
        });

        jButton2.setText("+");
        jButton2.setName(""); // NOI18N
        jButton2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ZoomIn(evt);
            }
        });

        jButton3.setText("-");
        jButton3.setName(""); // NOI18N
        jButton3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ZoomOut(evt);
            }
        });

        jButton4.setText("2D");
        jButton4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                _2D(evt);
            }
        });

        jButton5.setText("3D");
        jButton5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                _3D(evt);
            }
        });

        jToggleButton1.setText("View Weights");
        jToggleButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                viewWeights(evt);
            }
        });

        jButton6.setText("Screenshot");
        jButton6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                screenshot(evt);
            }
        });

        jButton7.setText("[ ]");
        jButton7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                zoomFit(evt);
            }
        });

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jButton8.setText("Load graph from file");
        jButton8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                loadGraphFromFile(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addComponent(jButton8)
                .addGap(18, 18, 18)
                .addComponent(jButton2)
                .addGap(57, 57, 57)
                .addComponent(jButton7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton3)
                .addGap(40, 40, 40))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addGap(67, 67, 67))
            .addGroup(layout.createSequentialGroup()
                .addGap(114, 114, 114)
                .addComponent(jButton4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton5)
                .addGap(56, 56, 56))
            .addGroup(layout.createSequentialGroup()
                .addGap(63, 63, 63)
                .addComponent(jToggleButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton6)
                .addGap(72, 72, 72))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(35, 35, 35)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2)
                    .addComponent(jButton3)
                    .addComponent(jButton7)
                    .addComponent(jButton8))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton4)
                    .addComponent(jButton5))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addComponent(jToggleButton1))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(47, 47, 47)
                        .addComponent(jButton6)))
                .addContainerGap(49, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void graphFrameClose(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_graphFrameClose
        frame.dispose();
        mode = "close";
        viewer.close();
    }//GEN-LAST:event_graphFrameClose

    private void ZoomIn(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ZoomIn
        view.getCamera().setViewPercent(view.getCamera().getViewPercent() - 0.1);
    }//GEN-LAST:event_ZoomIn

    private void ZoomOut(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ZoomOut
        view.getCamera().setViewPercent(view.getCamera().getViewPercent() + 0.1);
    }//GEN-LAST:event_ZoomOut

    private void _3D(java.awt.event.MouseEvent evt) {//GEN-FIRST:event__3D
        request_change = true;
        viewer.enableAutoLayout();
        request_change = false;
    }//GEN-LAST:event__3D

    private void _2D(java.awt.event.MouseEvent evt) {//GEN-FIRST:event__2D
        request_change = true;
        viewer.disableAutoLayout();
        request_change = false;
    }//GEN-LAST:event__2D

    private void viewWeights(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_viewWeights
        if (jToggleButton1.isSelected()) {
            graph.setAttribute("ui.stylesheet", "edge { "
                    + "text-visibility-mode:normal;"
                    + "}");
        } else {
            graph.setAttribute("ui.stylesheet", "edge { "
                    + "text-visibility-mode:hidden;"
                    + "}");
        }
    }//GEN-LAST:event_viewWeights

    private void screenshot(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_screenshot
        graph.addAttribute("ui.screenshot", ".\\..\\GraphStream.png");
    }//GEN-LAST:event_screenshot

    private void zoomFit(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_zoomFit
        view.getCamera().setViewPercent(1);
        set_styleSheet();
        view.getCamera().resetView();
        for (org.graphstream.graph.Node node : graph) {
            node.addAttribute("ui.size", 24);
            node.removeAttribute("ui.color");
        }
        // algroerth_on = false;

    }//GEN-LAST:event_zoomFit

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        mode = (String) jComboBox1.getSelectedItem();
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void loadGraphFromFile(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_loadGraphFromFile
        try {
            loadFromFile();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MainPlatform.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_loadGraphFromFile

    private void loadFromFile() throws FileNotFoundException {
        jFileChooser1.setDialogTitle("Open Source File");

        jFileChooser1.setAcceptAllFileFilterUsed(false);
        FileNameExtensionFilter restrict = new FileNameExtensionFilter(".txt files", "txt");
        jFileChooser1.addChoosableFileFilter(restrict);

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
                + "text-color:#4C3C57;"
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

    public static void color_generator(int input, Node node) {
        int reminder;
        int size;
        if (input > 0 && input < 10) {
            reminder = input % 10;// 1 2 3 4 5 6 7 8 9
            size = reminder * 2 + 20;

            node.changeAttribute("ui.color", Color.decode("#ffff00"));
            node.changeAttribute("ui.size", size);
            //#ffff00
        } else if ((input >= 10 && input < 20)) {
            reminder = input % 10;// 0 1 2 3 4 5 6 7 8 9 
            size = reminder * 2 + 20;

            node.changeAttribute("ui.color", Color.decode("#ffae42"));
            node.changeAttribute("ui.size", size);
            //#ffae42 

        } else if ((input >= 20 && input < 30)) {
            reminder = input % 10;// 0 1 2 3 4 5 6 7 8 9 
            size = reminder * 2 + 20;
            node.changeAttribute("ui.color", Color.decode("#FF7200"));
            node.changeAttribute("ui.size", size);
            //#FFA500

        } else if ((input >= 30 && input < 40)) {
            reminder = input % 10;// 0 1 2 3 4 5 6 7 8 9 
            size = reminder * 2 + 20;
            node.changeAttribute("ui.color", Color.decode("#ff4500"));
            node.changeAttribute("ui.size", size);
            //#ff4500 

        } else if ((input >= 40 && input < 50)) {
            reminder = input % 10;// 0 1 2 3 4 5 6 7 8 9 
            size = reminder * 2 + 20;
            node.changeAttribute("ui.color", Color.decode("#ff0000"));
            node.changeAttribute("ui.size", size);
            //#ff0000  

        } else if ((input >= 50 && input < 60)) {
            reminder = input % 10;// 0 1 2 3 4 5 6 7 8 9 
            size = reminder * 2 + 20;
            node.changeAttribute("ui.color", Color.decode("#c71585"));
            node.changeAttribute("ui.size", size);
            //#c71585   

        } else if ((input >= 60 && input < 70)) {
            reminder = input % 10;// 0 1 2 3 4 5 6 7 8 9 
            size = reminder * 2 + 20;
            node.changeAttribute("ui.color", Color.decode("#800080"));
            node.changeAttribute("ui.size", size);
            //#800080   

        } else if ((input >= 70 && input < 80)) {
            reminder = input % 10;// 0 1 2 3 4 5 6 7 8 9 
            size = reminder * 2 + 20;
            node.changeAttribute("ui.color", Color.decode("#8a2be2"));
            node.changeAttribute("ui.size", size);
            //#8a2be2    

        } else if ((input >= 80 && input < 90)) {
            reminder = input % 10;// 0 1 2 3 4 5 6 7 8 9 
            size = reminder * 2 + 20;
            node.changeAttribute("ui.color", Color.decode("#0000ff"));
            node.changeAttribute("ui.size", size);
            //#0000ff     

        } else if ((input >= 90 && input < 100)) {
            reminder = input % 10;// 0 1 2 3 4 5 6 7 8 9 
            size = reminder * 2 + 20;
            node.changeAttribute("ui.color", Color.decode("#0d98ba"));
            node.changeAttribute("ui.size", size);
            //#0d98ba      

        } else {

            //error
        }

    }

    public void initializeComboBox() {
        jComboBox1.removeAllItems();
        jComboBox1.addItem("Add Edge");
        jComboBox1.addItem("Add Node");
        jComboBox1.addItem("Node Edges");
        jComboBox1.addItem("Remove Node");
        jComboBox1.addItem("Remove Edge");
        jComboBox1.addItem("Free Move");
        jComboBox1.addItem("Change Weight");

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JFileChooser jFileChooser1;
    private javax.swing.JToggleButton jToggleButton1;
    // End of variables declaration//GEN-END:variables
}
