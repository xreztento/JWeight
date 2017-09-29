package org.weight.jmeter.common;

import java.io.IOException;

public interface Accessor {
	public String getConnectorName();
	public void open(String url, String username, String password) throws IOException;//������
	public Object access() throws IOException, AccessException;//ͨ�����������ʣ������ؽ������
	public void shutdown() throws IOException, AccessException;//�ر�����
}
