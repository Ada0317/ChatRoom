package msg;


import dataBase.Figures;

/*
 * ������Ӧ��ע��״̬��Ϣ��
 */
public class MsgRegResp extends MsgHead {
	private byte state;

	public byte getState() {
		return state;
	}

	public void setState(byte state) {
		this.state = state;
	}

	public MsgRegResp() {}

	public MsgRegResp(int jkDest, byte state) {
		setTotalLen(14);
		setType((byte)0x11);
		setDest(jkDest);
		setSrc(Figures.ServerJK);
		setState(state);
	}

}
