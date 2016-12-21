package msg;

public class MsgAddFriendResp extends MsgHead{
	private byte state;

	public byte getState() {
		return state;
	}

	public void setState(byte state) {
		this.state = state;
	}
}
