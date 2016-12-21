package msg;

import dataBase.Figures;

/*
 * 此消息体为登陆状态返回
 */
public class MsgLoginResp extends MsgHead {
	public byte getState() {
		return state;
	}

	public void setState(byte state) {
		this.state = state;
	}

	private byte state;

	public MsgLoginResp(boolean check) {
		setTotalLen(14);
		setType((byte)0x22);
		setDest(Figures.LoginJK);
		setSrc(Figures.ServerJK);
		byte state = 1;
		if (check) {
			state = 0;
		}
		setState(state);
	}


}
