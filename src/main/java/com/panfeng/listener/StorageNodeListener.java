package com.panfeng.listener;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.csource.common.IniFileReader;
import org.csource.common.MyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.paipianwang.pat.common.util.ValidateUtil;
import com.panfeng.dao.StorageLocateDao;

@SuppressWarnings("rawtypes")
@Component
public class StorageNodeListener implements ApplicationListener{

	@Autowired
	private final StorageLocateDao storageDao = null;
	
	private static final String CONF_FILENAME = Thread.currentThread().getContextClassLoader().getResource("").getPath() + "fdfs/fdfs_storage.conf";
	private IniFileReader iniReader;
	private String[] storageServers;
	
	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		// 是否是上下文刷新事件
		if(event instanceof ContextRefreshedEvent){
			try {
				Map<String, String> storageNodeMap = new HashMap<String, String>();
				storageNodeMap = init(CONF_FILENAME);
				if(!storageNodeMap.isEmpty()) {
					storageDao.resetStorageFromRedis(storageNodeMap);
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (MyException e) {
				e.printStackTrace();
			}
			
		}
		
	}
	
	public Map<String,String> init(final String confName) throws FileNotFoundException, IOException, MyException{ 
  		iniReader = new IniFileReader(confName);
  		storageServers = iniReader.getValues("storage_server");
  		if (storageServers == null)
  		{
  			throw new RuntimeException("item \"storage_server\" in " + confName + " not found");
  		}
  		
  		final Map<String, String> storageNodeMap = new HashMap<String, String>();
  		for(String storageServer : storageServers) {
  			if(ValidateUtil.isValid(storageServer)) {
  				final String[] args = storageServer.split(",");
  				storageNodeMap.put(args[0], args[1]);
  			}
  		}
  		return storageNodeMap;
	}

}
