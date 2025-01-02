
class DfsNode {
	byte id; //節點編號
	boolean visited = false; //已拜訪
	byte nn = -1; //下一節點
	byte pn = -1; //前一節點
	int hop = -1; //跳數
	
	//建構子
	DfsNode(byte id){
		this.id = id;
	}
	
	public void reset(){
		visited = false;
		pn = -1;
		nn = -1;
		hop = -1;
	}
}
