package msg;

import dataBase.Figures;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

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

	public MsgLoginResp() {}
	public MsgLoginResp(byte checkmsg) {
		setTotalLen(14);
		setType((byte)0x22);
		setDest(Figures.LoginJK);
		setSrc(Figures.ServerJK);
		setState(checkmsg);
	}
	public byte[] packMessage() throws IOException {
		ByteArrayOutputStream bous = new ByteArrayOutputStream();
		DataOutputStream dous = new DataOutputStream(bous);
		packMessageHead(dous);
		dous.write(getState());
		dous.flush();
		byte[] data = bous.toByteArray();
		return data;
	}

}
