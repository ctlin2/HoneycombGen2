import java.util.ArrayList;

class FCCModel {
	private ArrayList<FCCNode> m_Nodes;
	
	//建構子
	FCCModel(){
		m_Nodes = new ArrayList<FCCNode>();
	}
	
	public void add(FCCNode node){
		m_Nodes.add(node);
	}
	
	public ArrayList<FCCNode> getNodes(){
		return m_Nodes;
	}
}
