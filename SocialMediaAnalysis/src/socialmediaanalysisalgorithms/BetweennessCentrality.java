package socialmediaanalysisalgorithms;
//============import section===============//

import datastructure.Graph_Imp;
import datastructure.Node_Imp;
import java.util.Comparator;
import java.util.*;

//============================================================================//
public class BetweennessCentrality extends Graph_Imp implements CentralityAnalysis {

    //============data section================================================//
    private MyPair[][] src_shortest_path;
    //========================================================================//

    //========================over write methods section======================//
    public BetweennessCentrality() {
        super();
    }

    public BetweennessCentrality(int noVertices) {
        super(noVertices);
    }

    public BetweennessCentrality(Graph_Imp _graph) {
        vertices = _graph.getVertices();
    }

    @Override
    public void calculation() {
        src_shortest_path = dijkestra_for_all_H();
        for (int i = 0; i < getNoVertices(); i++) {
            getNode(i).setCentrality(YOU_PASS_THROW_ME(getNode(i)));
        }
    }

    //=========================internal class section=========================//
    public class Compare implements Comparator<Dijkestra_Data_Type> {
        // Overriding compare()method of Comparator  
        // for descending order of cgpa 

        public int compare(Dijkestra_Data_Type s1, Dijkestra_Data_Type s2) {
            if (s1.key() > s2.key()) {
                return 1;
            } else if (s1.key() < s2.key()) {
                return -1;
            }
            return 0;
        }
    }

    public class MyPair {

        private double key;
        private int value;

        public MyPair(Double aKey, int aValue) {
            key = aKey;
            value = aValue;
        }

        public MyPair() {
            key = 0;
            value = 0;
        }

        public Double key() {
            return key;
        }

        public void set(double key_, int value_) {
            key = key_;
            value = value_;
        }

        public int value() {
            return value;
        }
    }

    public class Dijkestra_Data_Type {

        private double key;//weight
        private int value;//me
        private int parent;//my parent

        public Dijkestra_Data_Type(Double aKey, int aValue, int aparent) {
            key = aKey;
            value = aValue;
            parent = aparent;
        }

        public Dijkestra_Data_Type() {
            key = 0;
            value = 0;
            parent = 0;
        }

        public Double key() {
            return key;
        }

        public void set(double key_, int value_) {
            key = key_;
            value = value_;
        }

        public int value() {
            return value;
        }

        public int parent() {
            return parent;
        }
    }
    //========================================================================//

    //===================public methods ======================================//
    public MyPair[] dijkestra_H(Node_Imp input) {

        // data section//
        boolean[] Marked_List = new boolean[getNoVertices()];
        MyPair[] out_put = new MyPair[getNoVertices()];
        PriorityQueue<Dijkestra_Data_Type> P_Queue = new PriorityQueue<Dijkestra_Data_Type>(new Compare());//priority queue

        //========================================================================================//
        // first time fill need optmization later//
        Marked_List[get_node_index(input)] = true;
        out_put[get_node_index(input)] = new MyPair(0.0, 1);// first time 

        for (int i = 0; i < input.getNoChildren(); i++) {
            P_Queue.add(new Dijkestra_Data_Type(input.getChildren_byIndex(i).getWeight(),
                    get_node_index(input.getChildren_byIndex(i).getChild()),
                    get_node_index(input)));
        }

        // operation section //
        while (!P_Queue.isEmpty()) {
            // data section//

            Dijkestra_Data_Type Out = P_Queue.poll();// got the peek
            int Next_Mark = Out.value();// get the next to mark
            int My_parent = Out.parent();//get my parent
            double Next_Weight = Out.key();//get the weight
            //================================================================//
            if (Marked_List[Next_Mark] == false) {

                Marked_List[Next_Mark] = true;// mark it visited

                out_put[Next_Mark] = new MyPair(Next_Weight, out_put[My_parent].value());// get the number of pathes like my parent

                // add the inserted childrens
                for (int i = 0; i < getNode(Next_Mark).getNoChildren(); i++) {

                    P_Queue.add(new Dijkestra_Data_Type(
                            getNode(Next_Mark).getChildren_byIndex(i).getWeight() + Next_Weight,
                            get_node_index(getNode(Next_Mark).getChildren_byIndex(i).getChild()),
                            get_node_index(getNode(Next_Mark))));

                }

            } else {
                int Already_Marked = Out.value();
                int My_parent_new = Out.parent();//get my parent
                double New_Weight = Out.key();

                if (out_put[Already_Marked].key() == New_Weight) {
                    out_put[Next_Mark].set(New_Weight,
                            out_put[Next_Mark].value() + out_put[My_parent_new].value());
                }

            }
        }// for loop on all vertices
        return out_put;
    }

    public MyPair[][] dijkestra_for_all_H() {
        MyPair[][] out_put = new MyPair[getNoVertices()][getNoVertices()];
        for (int i = 0; i < getNoVertices(); i++) {
            out_put[i] = dijkestra_H(getNode(i));
        }

        return out_put;

    }

    //==========================main method===================================//
    public double YOU_PASS_THROW_ME(Node_Imp wanted) {
        //=======================data section ================================//
        MyPair[] wanted_shortest_path = src_shortest_path[get_node_index(wanted)];
        double out = 0.0;
        boolean[][] marked = new boolean[getNoVertices()][getNoVertices()];
        int wanted_id = get_node_index(wanted);
        //====================================================================//

        for (int i = 0; i < getNoVertices(); i++) {
            if (i == wanted_id) {
                continue;
            }
            for (int j = 0; j < getNoVertices(); j++) {
                if (src_shortest_path[i][j].key() == (src_shortest_path[i][wanted_id].key() + wanted_shortest_path[j].key()) && j != wanted_id) {
                    if (marked[j][i]) {
                        continue;
                    }
                    marked[i][j] = true;
                    out += (double) (((double) src_shortest_path[wanted_id][j].value() * (double) src_shortest_path[i][wanted_id].value()) / (double) src_shortest_path[i][j].value());
                }//if
            }//for
        }//for
        //==========================================//
        return out;

    }
}
