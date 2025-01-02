import java.util.ArrayList;

class FCCModel {
	private ArrayList<FCCNode> m_Nodes;
	
	//«Øºc¤l
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