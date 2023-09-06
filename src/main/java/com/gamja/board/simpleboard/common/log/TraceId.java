package com.gamja.board.simpleboard.common.log;

import java.util.UUID;

public class TraceId {

	private String id;

	public TraceId() {
		this.id = createId();
	}

	public String getId() {
		return id;
	}

	private String createId() {
		return UUID.randomUUID().toString().substring(0, 8);
	}
}
