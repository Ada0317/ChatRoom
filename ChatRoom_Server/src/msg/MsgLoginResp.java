package msg;

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

}
