package com.panfeng.service;

import com.panfeng.resource.model.IndentResource;

public interface OnlineDocService {

	public String getFile(IndentResource indentResource);

	public String convertFile(IndentResource indentResource);
}
