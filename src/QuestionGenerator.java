import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;
import java.util.StringTokenizer;

class QuestionGenerator {
	public FCCModel m_FccModel;
	private int m_Level = 1;
	final static int m_GivenNumber = 2; //事先限定的數字個數
	final static int m_MaxFirst = 8; //事先限定的最小數字上限
	private boolean[][] m_Adj; // 鄰接矩陣
	private byte[][] m_Dist; //距離矩陣
	private DfsNode[] m_DfsNodes; // 節點狀態
	private byte[][] m_Table; // 旋轉對照表
	private Stack<DfsNode> m_Stack; // 堆疊
	
	private byte[][] m_Candidates = new byte[15236944][]; // 記錄篩選後的可能路徑
	private int m_Count = 0; // 記錄篩選後的可能路徑數
	
	private Stack<Constraint> m_ConsStack = new Stack<Constraint>();
	private ArrayList<QA> m_QA = new ArrayList<QA>(); // 記錄題目與解答
	
	private static BufferedReader m_Reader;
	private static PrintWriter m_Writer;
	
	// 建構子
	QuestionGenerator() {
		try {
			m_Writer = new PrintWriter("qa.txt");
			m_Reader = new BufferedReader(new FileReader("qa1.txt"));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			return;
		} catch (Exception e2){
			e2.printStackTrace();
		}
		
		loadFile();
		
		m_FccModel = new FCCModel();
		
		addFccNodes(m_Level);
		
		m_Adj = createAdjacencyMatrix();
		
		m_Dist = createDistances();

		// 為每個節點建立狀態
		m_DfsNodes = createDfsNodes();
		m_Stack = new Stack<DfsNode>();
		
		m_Table = createRotationTable(m_Level);
		
		int run = 0;
		while (run < 5){
			int m = m_FccModel.getNodes().size();
			m_Count = 0;
			// generate paths
			byte[] b = new byte[m];
			genConstraint(b);
			
			//dump constraint
			System.out.print("init:{");
			for (int i = 0; i < m; i++){
				System.out.print(i==0 ? b[i] : ", "+ b[i]);
			}
			System.out.println("}");

			if (badDistance(b)) {
				continue;
			}
			
			if (genPaths(b) > 0){ // 找到一些吻合的候選路徑
				// DEBUG: validate
				for (int i = 0; i < m_Count; i++){
					for (int p = 0; p < b.length; p++){
						if (b[p]!= 0 && b[p] != m_Candidates[i][p]){
							System.out.println("Error:"+i);
							dumpPath(m_Candidates[i]);
							try {
								System.in.read();
							} catch (IOException e) {
								// TODO 自動產生的 catch 區塊
								e.printStackTrace();
							}
						}
					}
				}
				
				// 增加限定條件，篩選至唯一條路徑
				ScreenPaths(b, m_Count);
				m_Writer.flush();
				
				//clean all paths
				for (int i = 0; i < m_Count; i++){
					m_Candidates[i] = null;
				}
			}
		}
		
		m_Writer.close();
		
		System.out.println("find "+ m_QA.size() + " questions.");
	}
	
	private void addFccNodes(int level){
		switch (level){
		case -1: // plain
			//*
			m_FccModel.add(new FCCNode(0, 0, -3, 3));
			m_FccModel.add(new FCCNode(0, 1, -3, 2));
			m_FccModel.add(new FCCNode(0, 2, -3, 1));
			m_FccModel.add(new FCCNode(0, 3, -3, 0));
			m_FccModel.add(new FCCNode(0, -1, -2, 3));
			m_FccModel.add(new FCCNode(0, 0, -2, 2));
			m_FccModel.add(new FCCNode(0, 1, -2, 1));
			m_FccModel.add(new FCCNode(0, 2, -2, 0));
			m_FccModel.add(new FCCNode(0, 3, -2, -1));
			m_FccModel.add(new FCCNode(0, -2, -1, 3));
			m_FccModel.add(new FCCNode(0, -1, -1, 2));
			m_FccModel.add(new FCCNode(0, 0, -1, 1));
			m_FccModel.add(new FCCNode(0, 1, -1, 0));
			m_FccModel.add(new FCCNode(0, 2, -1, -1));
			m_FccModel.add(new FCCNode(0, 3, -1, -2));
			m_FccModel.add(new FCCNode(0, -3, 0, 3));
			m_FccModel.add(new FCCNode(0, -2, 0, 2));
			m_FccModel.add(new FCCNode(0, -1, 0, 1));
			m_FccModel.add(new FCCNode(0, 0, 0, 0));
			m_FccModel.add(new FCCNode(0, 1, 0, -1));
			m_FccModel.add(new FCCNode(0, 2, 0, -2));
			m_FccModel.add(new FCCNode(0, 3, 0, -3));
			m_FccModel.add(new FCCNode(0, -3, 1, 2));
			m_FccModel.add(new FCCNode(0, -2, 1, 1));
			m_FccModel.add(new FCCNode(0, -1, 1, 0));
			m_FccModel.add(new FCCNode(0, 0, 1, -1));
			m_FccModel.add(new FCCNode(0, 1, 1, -2));
			m_FccModel.add(new FCCNode(0, 2, 1, -3));
			m_FccModel.add(new FCCNode(0, -3, 2, 1));
			m_FccModel.add(new FCCNode(0, -2, 2, 0));
			m_FccModel.add(new FCCNode(0, -1, 2, -1));
			m_FccModel.add(new FCCNode(0, 0, 2, -2));
			m_FccModel.add(new FCCNode(0, 1, 2, -3));
			m_FccModel.add(new FCCNode(0, -3, 3, 0));
			m_FccModel.add(new FCCNode(0, -2, 3, -1));
			m_FccModel.add(new FCCNode(0, -1, 3, -2));
			m_FccModel.add(new FCCNode(0, 0, 3, -3));
			//*/
			break;
		case 0:
			m_FccModel.add(new FCCNode(0,1,-2,1));
			m_FccModel.add(new FCCNode(0,1,-1,0));
			m_FccModel.add(new FCCNode(0,1,0,-1));
		
			m_FccModel.add(new FCCNode(0,0,-1,1));
			m_FccModel.add(new FCCNode(0,0,0,0));
			
			m_FccModel.add(new FCCNode(0,-1,0,1));

			
			m_FccModel.add(new FCCNode(1,0,-2,1));
			m_FccModel.add(new FCCNode(1,0,-1,0));
			
			m_FccModel.add(new FCCNode(1,-1,-1,1));

			
			m_FccModel.add(new FCCNode(2,-1,-2,1));
			break;
		case 2:
			m_FccModel.add(new FCCNode(0,1,-2,1));
			m_FccModel.add(new FCCNode(0,1,-1,0));
			m_FccModel.add(new FCCNode(0,1,0,-1));
			m_FccModel.add(new FCCNode(0,1,1,-2));
			m_FccModel.add(new FCCNode(0,1,2,-3));
			
			m_FccModel.add(new FCCNode(0,0,-1,1));
			m_FccModel.add(new FCCNode(0,0,0,0));
			m_FccModel.add(new FCCNode(0,0,1,-1));
			m_FccModel.add(new FCCNode(0,0,2,-2));
			
			m_FccModel.add(new FCCNode(0,-1,0,1));
			m_FccModel.add(new FCCNode(0,-1,1,0));
			m_FccModel.add(new FCCNode(0,-1,2,-1));
			
			m_FccModel.add(new FCCNode(0,-2,1,1));
			m_FccModel.add(new FCCNode(0,-2,2,0));
			
			m_FccModel.add(new FCCNode(0,-3,2,1));
			
			
			m_FccModel.add(new FCCNode(1,0,-2,1));
			m_FccModel.add(new FCCNode(1,0,-1,0));
			m_FccModel.add(new FCCNode(1,0,0,-1));
			m_FccModel.add(new FCCNode(1,0,1,-2));
			
			m_FccModel.add(new FCCNode(1,-1,-1,1));
			// m_FccModel.add(new FCCNode(1,-1,0,0)); // invisible
			m_FccModel.add(new FCCNode(1,-1,1,-1));
			
			m_FccModel.add(new FCCNode(1,-2,0,1));
			m_FccModel.add(new FCCNode(1,-2,1,0));
			
			m_FccModel.add(new FCCNode(1,-3,1,1));

			
			m_FccModel.add(new FCCNode(2,-1,-2,1));
			m_FccModel.add(new FCCNode(2,-1,-1,0));
			m_FccModel.add(new FCCNode(2,-1,0,-1));
			
			m_FccModel.add(new FCCNode(2,-2,-1,1));
			m_FccModel.add(new FCCNode(2,-2,0,0));
			
			m_FccModel.add(new FCCNode(2,-3,0,1));
			
			
			m_FccModel.add(new FCCNode(3,-2,-2,1));
			m_FccModel.add(new FCCNode(3,-2,-1,0));
			
			m_FccModel.add(new FCCNode(3,-3,-1,1));
			
			
			m_FccModel.add(new FCCNode(4,-3,-2,1));
			break;
		case 1:
		default:
			m_FccModel.add(new FCCNode(0,1,-2,1));
			m_FccModel.add(new FCCNode(0,1,-1,0));
			m_FccModel.add(new FCCNode(0,1,0,-1));
			m_FccModel.add(new FCCNode(0,1,1,-2));
			m_FccModel.add(new FCCNode(0,0,-1,1));
			m_FccModel.add(new FCCNode(0,0,0,0));
			m_FccModel.add(new FCCNode(0,0,1,-1));
			m_FccModel.add(new FCCNode(0,-1,0,1));
			m_FccModel.add(new FCCNode(0,-1,1,0));
			m_FccModel.add(new FCCNode(0,-2,1,1));
			
			m_FccModel.add(new FCCNode(1,0,-2,1));
			m_FccModel.add(new FCCNode(1,0,-1,0));
			m_FccModel.add(new FCCNode(1,0,0,-1));
			m_FccModel.add(new FCCNode(1,-1,-1,1));
			m_FccModel.add(new FCCNode(1,-1,0,0));
			m_FccModel.add(new FCCNode(1,-2,0,1));
					
			m_FccModel.add(new FCCNode(2,-1,-2,1));
			m_FccModel.add(new FCCNode(2,-1,-1,0));
			m_FccModel.add(new FCCNode(2,-2,-1,1));
			
			m_FccModel.add(new FCCNode(3,-2,-2,1));
			break;
		}
	}
	
	private void genConstraint(byte[] b){
		//generate m distinct positions
		byte[] p = new byte[b.length];
		for (byte i = 0; i < b.length; i++){
			p[i] = i;
		}
		for (int i = 0; i < m_GivenNumber; i++){
			int x = (int)(Math.random()*(b.length - i));
			// swap(p[i], p[i+x]);
			byte t = p[i];
			p[i] = p[i+x];
			p[i+x] = t;
		}
		
		// generate m distinct numbers
		byte[] n = new byte[b.length];
		for (byte i = 0; i < b.length; i++){
			n[i] = (byte)(i+1);
		}
		for (int i = 0; i < m_GivenNumber; i++){
			int x = (int)(Math.random()*(i == 0 ? m_MaxFirst : b.length - i));
			// swap(p[i], p[i+x]);
			byte t = n[i];
			n[i] = n[i+x];
			n[i+x] = t;
		}
		
		for (int i = 0; i < m_GivenNumber; i++){
			b[p[i]] = n[i];
		}
	}
	
	private int genPaths(byte[] q){
		// 根據題目 q 建立一個號碼對位置的對應表 qT
		final byte[] qT = new byte[q.length + 1]; 
		for (byte i = 0; i < qT.length; i++){
			qT[i] = -1;
		}
		for (byte i = 0; i < q.length; i++){
			if (q[i] != 0){
				qT[q[i]] = i;
			}
		}
		
		for (int i = 0; i < q.length; i++){
			System.out.print("Start=" + (i + 1)); //DEBUG
			int c = DFS((byte)i, qT);
			System.out.println(" count=" + c); //DEBUG
		}	
		
		
		System.out.println("Total candidate: "+ m_Count + " paths.");
		return m_Count;
	}
	
	private boolean[][] createAdjacencyMatrix() {
		ArrayList<FCCNode> nodes = m_FccModel.getNodes();
		int m = nodes.size();
		boolean[][] a = new boolean[m][m]; // default: false
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < i; j++) {
				if (nodes.get(i).isNeighbor(nodes.get(j))) {
					a[i][j] = a[j][i] = true;
				}
			}
		}
		return a;
	}

	public boolean[][] getAdjacencyMatrix() {
		return m_Adj;
	}
	
	private byte[][] createDistances(){
		int m = m_FccModel.getNodes().size();
		byte[][] distTo = new byte[m][m];
		//intialize distances using adjacecy matrix
		for (byte i = 0; i < m; i++) {
            for (byte j = 0; j < i; j++) {
            	if (m_Adj[i][j]){
            		distTo[i][j] = distTo[j][i] = 1;
            		// edgeTo[i][j] = j;
            		// edgeTo[j][i] = i;
            	}
            	else {
            		distTo[i][j] = Byte.MAX_VALUE;
            	}
            }
            distTo[i][i] = 0;
        }
		
		// Floyd-Warshall updates
        for (byte i = 0; i < m; i++) {
            // compute shortest paths using only 0, 1, ..., i as intermediate vertices
            for (byte v = 0; v < m; v++) {
                if (!m_Adj[v][i]) continue;  // optimization
                for (byte w = 0; w < m; w++) {
                    if (distTo[v][w] > distTo[v][i] + distTo[i][w]) {
                        distTo[v][w] = (byte)(distTo[v][i] + distTo[i][w]);
                        // edgeTo[v][w] = i;
                    }
                }
            }
        }
        
        //DEBUG
        System.out.println("Distance Matrix:");
        for (byte i = 0; i < m; i++){
        	for (byte j = 0; j < m; j++){
        		System.out.print(distTo[i][j]+ " ");
        	}
        	System.out.println();
        }
        
        return distTo;
	}
	
	private boolean badDistance(byte[] q){
		final byte[] qT = new byte[q.length + 1]; 
		for (byte i = 0; i < qT.length; i++){
			qT[i] = -1;
		}
		for (byte i = 0; i < q.length; i++){
			if (q[i] != 0){
				qT[q[i]] = i;
			}
		}
		
		// DEBUG
		System.out.print("qT:");
		for (int i = 1; i < qT.length; i++){
			System.out.print(qT[i]+" ");
		}
		System.out.println();
		
		byte i = 0;
		byte j = 0;
		
		while (qT[i] == -1) i++;
		for (j = (byte)(i + 1); j < qT.length; j++){
			if (qT[j] == -1) continue;
			
			if (m_Dist[qT[i]][qT[j]] > (j - i)){ // too far
				System.out.println("(p,n):from:("+qT[i]+","+ i + ") to:(" + qT[j] +"," + j+") too far!");
				return true;
			}
			else {
				i = j;
			}
		}
		return false;
	}

	private DfsNode[] createDfsNodes() {
		ArrayList<FCCNode> nodes = m_FccModel.getNodes();
		int n = nodes.size();
		DfsNode[] a = new DfsNode[n];
		for (byte i = 0; i < n; i++) {
			a[i] = new DfsNode(i);
		}
		return a;
	}
	
	//產生旋轉對照表
	private byte[][] createRotationTable(int level){
		byte[][] table = null;
		switch(level){
		case -1:
			table = new byte[6][37];
			byte[] p = {4, 9, 15, 22, 
					3, 8, 14, 21, 28, 
					2, 7, 13, 20, 27, 33, 
					1, 6, 12, 19, 26, 32, 37, 
					5, 11, 18, 25, 31, 36, 
					10, 17, 24, 30, 35, 
					16, 23, 29, 34};

			for (byte j = 0; j < 37; j++){
				table[0][j] = (byte)(j + 1);
				// System.out.print(table[0][j] + " "); //DEBUG
			}
			// System.out.println(); //DEBUG
			
			for (byte i = 1; i < 6; i++){
				for (byte j = 0; j < 37; j++){
					table[i][j] = p[table[i-1][j]-1];
					// System.out.print(table[i][j] + " "); //DEBUG
				}
				// System.out.println(); //DEBUG
			}
			break;
		case 0: // todo
			table = new byte[][]{
				{1,2,3,4,5,6,7,8,9,10},
				{1,7,10,2,8,3,4,9,5,6},
				{1,4,6,7,9,10,2,5,8,3},
				{3,5,6,2,4,1,8,9,7,10},
				{3,8,10,5,9,6,2,7,4,1},
				{3,2,1,8,7,10,5,4,9,6},
				{6,4,1,5,2,3,9,7,8,10},
				{6,9,10,4,7,1,5,8,2,3},
				{6,5,3,9,8,10,4,2,7,1},
				{10,7,1,9,4,6,8,2,5,3},
				{10,8,3,7,2,1,9,5,4,6},
				{10,9,6,8,5,3,7,4,2,1}
			};
			break;
		case 1:
			table = new byte[][]{
					{1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20},
					{1,5,8,10,11,14,16,17,19,20,2,6,9,12,15,18,3,7,13,4},
					{1,11,17,20,2,12,18,3,13,4,5,14,19,6,15,7,8,16,9,10},
					{4,7,9,10,3,6,8,2,5,1,13,15,16,12,14,11,18,19,17,20},
					{4,3,2,1,13,12,11,18,17,20,7,6,5,15,14,19,9,8,16,10},
					{4,13,18,20,7,15,19,9,16,10,3,12,17,6,14,8,2,11,5,1},
					{10,8,5,1,9,6,2,7,3,4,16,14,11,15,12,13,19,17,18,20},
					{10,9,7,4,16,15,13,19,18,20,8,6,3,14,12,17,5,2,11,1},
					{10,16,19,20,8,14,17,5,11,1,9,15,18,6,12,2,7,13,3,4},
					{20,17,11,1,19,14,5,16,8,10,18,12,2,15,6,9,13,3,7,4},
					{20,19,16,10,18,15,9,13,7,4,17,14,8,12,6,3,11,5,2,1},
					{20,18,13,4,17,12,3,11,2,1,19,15,7,14,6,5,16,9,8,10}
			};
			break;
		case 2: 
			table = new byte[][]{
				{1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34},
				{1,16,25,31,34,2,17,26,32,3,18,27,4,19,5,6,20,28,33,7,29,8,21,9,10,22,30,11,23,12,13,24,14,15},
				{1,6,10,13,15,16,20,22,24,25,28,30,31,33,34,2,7,11,14,17,23,26,29,32,3,8,12,18,21,27,4,9,19,5},
				{5,9,12,14,15,4,8,11,13,3,7,10,2,6,1,19,21,23,24,18,22,17,20,16,27,29,30,26,28,25,32,33,31,34},
				{5,19,27,32,34,9,21,29,33,12,23,30,14,24,15,4,18,26,31,8,28,11,22,13,3,17,25,7,20,10,2,16,6,1},
				{5,4,3,2,1,19,18,17,16,27,26,25,32,31,34,9,8,7,6,21,20,29,28,33,12,11,10,23,22,30,14,13,24,15},
				{15,13,10,6,1,14,11,7,2,12,8,3,9,4,5,24,22,20,16,23,17,21,18,19,30,28,25,29,26,27,33,31,32,34},
				{15,24,30,33,34,13,22,28,31,10,20,25,6,16,1,14,23,29,32,11,26,7,17,2,12,21,27,8,18,3,9,19,4,5},
				{15,14,12,9,5,24,23,21,19,30,29,27,33,32,34,13,11,8,4,22,18,28,26,31,10,7,3,20,17,25,6,2,16,1},
				{34,31,25,16,1,33,28,20,6,30,22,10,24,13,15,32,26,17,2,29,7,23,11,14,27,18,3,21,8,12,19,4,9,5},
				{34,32,27,19,5,31,26,18,4,25,17,3,16,2,1,33,29,21,9,28,8,20,7,6,30,23,12,22,11,10,24,14,13,15},
				{34,33,30,24,15,32,29,23,14,27,21,12,19,9,5,31,28,22,13,26,11,18,8,4,25,20,10,17,7,3,16,6,2,1}
			};
		}
		
		return table;
	}
	
	//第4版
	public int DFS(byte start, byte[] qT) {
		// 檢查距離，因為在start的數字是1，若start 到 給定 最小數字位置，距離超過數字差，則不可能達到
		int i = 0;
		while (qT[i] == -1) i++;
		if ((m_Dist[start][qT[i]]) > i - 1 )
			return 0;
		
		// 用 DFS 找出由編號 start節點 出發，且吻合題目 q 的完全路徑
		m_DfsNodes[start].hop = 1;
		m_DfsNodes[start].nn = 0;
		m_DfsNodes[start].visited = true;
		m_Stack.push(m_DfsNodes[start]);

		return search(qT);
	}
	
	private int search(byte[] qT) {
		int m = m_FccModel.getNodes().size();
		int count = 0; // 發現到的路徑數
		
		while (!m_Stack.isEmpty()) {
			DfsNode a = m_Stack.peek();
			byte pos = qT[a.hop];
			if (pos != -1){ // 題目有指定此數字的位置
				//比較位置是否吻合
				if (a.id != pos){
					//不吻合，不用再比了，回退
					a = m_Stack.pop();
					a.reset();
					continue;
				}
			}
			
			if (a.hop >= m) {
				// 找到一條完全路徑(且符合條件), 記入題庫
				try {
					count++;
					m_Candidates[m_Count++] = getPath((byte)m, a);
					a = m_Stack.pop(); // 回退
					a.reset();
				}
				catch (ArrayIndexOutOfBoundsException ex){
					//超出陣列容量，放棄
					// 清除堆疊，並還原所有節點狀態
					while (!m_Stack.isEmpty()){
						a = m_Stack.pop();
						a.reset();
					}
					
					System.out.println("超出陣列容量");
					return 0; 
				}
			} else {
				// 找下一個可以前進的未拜訪鄰居
				while (a.nn < m && (!m_Adj[a.id][a.nn] || m_DfsNodes[a.nn].visited))
					a.nn++;

				if (a.nn < m) { // 有下一個可拜訪鄰居的鄰居 b
					DfsNode b = m_DfsNodes[a.nn];
					a.nn++; // 更新下次準備拜訪的節點編號
					b.pn = a.id;
					b.hop = a.hop + 1;
					b.nn = 0;
					b.visited = true; //
					m_Stack.push(b);
				} else { // 已無可拜訪的鄰居
					a = m_Stack.pop(); // 回退
					a.reset(); // 狀態還原
				}
			}
		}
		return count; // 回傳找到的路徑數
	}
	
	private byte[] getPath(byte n, DfsNode a) {
		// 依前一個節點，追蹤整條路徑
		byte[] answer = new byte[n]; // 記錄每個節點的在路徑中的順位
		DfsNode tempNode = a;
		answer[tempNode.id] = n--;
		// System.out.print(permute[tempNode.id] + " "); // DEBUG
		while (tempNode.pn != -1) {
			tempNode = m_DfsNodes[tempNode.pn];
			answer[tempNode.id] = n--;
			// System.out.print(permute[tempNode.id] + " "); // DEBUG
		}
		
		// System.out.println(); // DEBUG
		
		return answer;
	}
	
	// 根據 fixed條件，篩選路徑，並挑選新的數字n與位置p，使剩餘數徑數最少
	private void ScreenPaths(byte[] fixed, int count){
		
		m_ConsStack.push(new Constraint(fixed, count));
		while (!m_ConsStack.isEmpty()){
			Constraint constraint = m_ConsStack.pop();
			if (constraint.remainPaths > 1){
				screen(constraint.c, constraint.remainPaths);
			}
			else if (constraint.remainPaths == 1){
				// write path
				dumpPath(constraint.c); // for DEBUG
				byte[] answer = dumpPathDetail(constraint.c); // Lookup the exact path
				QA qa = new QA(constraint.c, answer);
				
				// check if q is in the QA list already (include rotations)
				boolean found = false;
				for (QA x : m_QA){ // x is in the list
					for (int r = 0; r < m_Table.length; r++){ // for each rotation
						byte[] y = new byte[qa.q.length];
						for (byte i = 0; i < qa.q.length; i++){
							y[i] = qa.q[m_Table[r][i]-1];
						}
						
						if (Arrays.equals(x.q, y))
							found = true;
					}
				}
				
				if (!found) {
					m_QA.add(qa);
					write2file(qa);
				}
				else {
					System.out.println("Duplicate. Drop this path.");
				}
			}
		}
	}
	
	private void screen(byte[] fixed, int remainCount) {
		int minCount = remainCount;
		
		ArrayList<NP> min_np = new ArrayList<NP>();

			for (byte n = 1; n <= fixed.length; n++){ // each number
				
				// checked n is used or not
				boolean found = false;
				for (byte i = 0; i < fixed.length; i++){
					if (fixed[i] == n) {
						found = true;
						break;
					}
				}
				if (found) continue;
				
				for (byte p = 0; p < fixed.length; p++){ // each position
					if (fixed[p] != 0) continue;
					int count = fix(p, n, fixed);
					System.out.print("("+n+","+(p+1)+","+count+")" ); // DEBUG
					if (count != 0){
						if (count < minCount){
							minCount = count;
							min_np.clear(); // todo: 保留一些最少的  by TreeSet
							min_np.add(new NP(n, p));
						}
						else if (count == minCount){
							min_np.add(new NP(n, p));
						}
					}
				}
				System.out.println(); // DEBUG
			}
			
			if (minCount > 0) {
				// 多種n與p使路徑剩餘數少，則
				int index = 0;
				for (NP np : min_np){
					if (index > 1  && index < min_np.size() - 2){
						//隨機放棄一些
						double x = Math.random();
						if (x > (10.0 - 4)/min_np.size())
							continue;
					}
					byte[] a = Arrays.copyOf(fixed, fixed.length);
					a[np.p] = np.n; //增加限制
					m_ConsStack.push(new Constraint(a, minCount) ); //列入篩選
					index++;
				}
			}
			else {
				System.out.println("Error: minCount=0");
			}
			/* int index = (int)(Math.random() * min_np.size());
			//int index = 0;
			NP x = min_np.get(index);
				
			System.out.println("min(n,p,count)=("+x.n+","+(x.p+1)+","+minCount+")");
			
			// remainCount = minCount;
			fixed[x.p] = x.n;
			
			min_np.clear();
			*/
		//}

	}
	
	private void dumpPath(byte[] fixed){
		System.out.print("Dump:{");
		
		for (int i = 0; i < fixed.length; i++){
			System.out.print(i == 0 ? fixed[i] : ", " + fixed[i]);
			
		}
		System.out.println("},");
		
	}
	
	private byte[] dumpPathDetail(byte[] fixed){
		byte[] b = new byte[fixed.length];
		for (int e = 0; e < m_Count; e++){
				// check if matched fixed
				boolean matched = true;
				for (int i = 0; i < fixed.length; i++){
					if (fixed[i] != 0 && m_Candidates[e][i] != fixed[i]){
						matched = false;
						break;
					}
				}
				
				if (matched){
					System.out.print("{"); // DEBUG
					
					for (int i = 0; i < fixed.length; i++){
						System.out.print(i == 0 ? 
								m_Candidates[e][i] :  
								", "+ m_Candidates[e][i]); // DEBUG
						
						b[i] = m_Candidates[e][i];
					}
					System.out.println("},"); // DEBUG
					
					break;
				}
		}
		return b;
	}
	
	private int fix(int p, byte n, byte[] fixed){
		int count = 0;
		for (int e = 0; e < m_Count; e++){
			// 檢查是否吻合 fixed 條件
			boolean matched = true;
			for (int i = 0; i < fixed.length; i++){
				if (fixed[i] != 0 && m_Candidates[e][i] != fixed[i]){
					matched = false;
					break;
				}
			}
			if (!matched) continue;
			
			if (m_Candidates[e][p] == (byte)n){ // 吻合
				count++;
			}	
		}
		
		return count;
	}
	
	private void write2file(QA qa){
		// write question
		byte[] b = qa.q;
		m_Writer.print("{");
		for (int i = 0; i < b.length; i++){
			m_Writer.print(i == 0 ? b[i] : ", "+ b[i]);
		}
		m_Writer.println("},");
		
		// write answer
		b = qa.a;
		m_Writer.print("{");
		for (int i = 0; i < b.length; i++){
			m_Writer.print(i == 0 ? b[i] : ", "+ b[i]);
		}
		m_Writer.println("},");
	}
	
	private void loadFile(){
		// merge the old paths
		try {
			while (m_Reader.ready()){
				String line1 = m_Reader.readLine();
				String line2 = m_Reader.readLine();
				StringTokenizer st = new StringTokenizer(line1, "{}, ");
				byte[] q = new byte[st.countTokens()];
				int i = 0;
				while(st.hasMoreTokens()){
					q[i++] = Byte.parseByte(st.nextToken());
				}
				
				st = new StringTokenizer(line2, "{}, ");
				byte[] a = new byte[st.countTokens()];
				i = 0;
				while(st.hasMoreTokens()){
					a[i++] = Byte.parseByte(st.nextToken());
				}
				
				QA qa = new QA(q, a);
				
				m_QA.add(qa);
				write2file(qa);
			}
			m_Reader.close();
		} catch (IOException e){
			e.printStackTrace();
		}
		finally {
			m_Writer.flush();
		}
	}
	
}

class QA {
	byte[] q;
	byte[] a;

	//建構子
	QA(byte[] q, byte[] a){
		this.q = q;
		this.a = a;
	}
}

class NP {
	byte n; // number
	byte p; //position
	
	NP(byte n, byte p){
		this.n = n;
		this.p = p;
	}
}

class Constraint {
	byte[] c;
	int remainPaths;
	
	Constraint(byte[] c, int count){
		this.c = c;
		this.remainPaths = count;
	}
}