package com.isslam.husonmuslim.controllers;

import java.util.ArrayList;
import java.util.HashMap;

import android.util.Log;

public class UndoManager {

	private static UndoManager instance = null;
	ArrayList<HashMap<String, Integer>> actionList = new ArrayList<HashMap<String, Integer>>();

	public static UndoManager getInstance() {
		if (instance == null) {
			instance = new UndoManager();
		}
		return instance;
	}

	// Constructor
	public UndoManager() {

	}

	Boolean toggleAdd = true;

	public void addUndoAction(int id, int val_1, int val_2) {
		Boolean checkLast = true;
		if (actionList.size() > 0) {
			if (actionList.get(actionList.size() - 1).get("_id") == id
					&& actionList.get(actionList.size() - 1).get("val_1") == val_1
					&& actionList.get(actionList.size() - 1).get("val_2") == val_2) {
				checkLast = false;
			}
		}

		if (checkLast && toggleAdd) {
			HashMap<String, Integer> map = new HashMap<String, Integer>();
			
			map.put("_id", id);
			map.put("val_1", val_1);
			map.put("val_2", val_2);

			actionList.add(map);
		}
		if (!toggleAdd)
			toggleAdd = true;

	}

	public HashMap<String, Integer> getUndoAction() {
		HashMap<String, Integer> map;

		if (!actionList.isEmpty()) {
			map = actionList.get(actionList.size() - 1);
			actionList.remove(actionList.size() - 1);

		} else {

			map = new HashMap<String, Integer>();
			map.put("_id", -1);

		}
		toggleAdd = true;
		return map;
	}

}
