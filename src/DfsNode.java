
class DfsNode {
	byte id; //�`�I�s��
	boolean visited = false; //�w���X
	byte nn = -1; //�U�@�`�I
	byte pn = -1; //�e�@�`�I
	int hop = -1; //����
	
	//�غc�l
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
