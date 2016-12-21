package msg;


import dataBase.Figures;

/*
 * 服务器应答注册状态消息体
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
