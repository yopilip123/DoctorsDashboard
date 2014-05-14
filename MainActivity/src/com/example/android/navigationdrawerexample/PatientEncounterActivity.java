/**
 * @author Jake Randolph B Muncada
 */

package com.example.android.navigationdrawerexample;

import java.util.ArrayList;

import android.app.ExpandableListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.Toast;

import com.example.database.DatabaseAdapter;
import com.example.model.Encounter;
import com.example.model.Soap;

public class PatientEncounterActivity extends ExpandableListActivity{

	private ArrayList<String> parentItems = new ArrayList<String>();
	private ArrayList<Object> childItems = new ArrayList<Object>();
	private ArrayList<Object> child;
	
	private final int INDEX_MEDICAL_HISTORY = 0;
	private final int INDEX_PREVIOUS_REQUESTS = 1;
	private final int INDEX_REFERRALS = 2;
	private final int INDEX_NOTES = 3;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		DatabaseAdapter db = new DatabaseAdapter(getApplicationContext());

		// this is not really  necessary as ExpandableListActivity contains an ExpandableList
		//setContentView(R.layout.main);

		ExpandableListView expandableList = getExpandableListView(); // you can use (ExpandableListView) findViewById(R.id.list)

		expandableList.setDividerHeight(2);
		expandableList.setGroupIndicator(null);
		expandableList.setClickable(true);

		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		
		int encounter_id = bundle.getInt("EXTRA_ENCOUNTER_ID");
		Encounter encounter = db.getEncounter(encounter_id);
		int patient_id = encounter.getPid();
		String date_encountered = encounter.getDateEncountered().trim().substring(0,10);
		
		setGroupParents();
		setChildData(patient_id, encounter_id, date_encountered);
		setDoctorsNotes(encounter_id);

		ExpListAdapter adapter = new ExpListAdapter(parentItems, childItems);

		adapter.setInflater((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE), this);
		expandableList.setAdapter(adapter);
		expandableList.setOnChildClickListener(new OnChildClickListener() {
			
			@Override
			public boolean onChildClick (ExpandableListView parent, View view, int groupPosition, int childPosition, long id) {
				Toast.makeText(getApplicationContext(), "BOOM! " + groupPosition + " " + childPosition, Toast.LENGTH_SHORT).show();
				switch (groupPosition) {
					case INDEX_MEDICAL_HISTORY:
						int eid = ((Encounter)child.get(childPosition)).getEncounterId();
						Bundle bundle = new Bundle();
						bundle.putInt("EXTRA_ENCOUNTER_ID", eid);
						Intent intent = new Intent(view.getContext(), PatientEncounterActivity.class);
						intent.putExtras(bundle);
						view.getContext().startActivity(intent);
						break;
					case INDEX_PREVIOUS_REQUESTS:
						break;
					case INDEX_REFERRALS:
						break;
					case INDEX_NOTES:
						break;
					default:
						Log.e("onClick groupPosition", "Error on groupPosition: Should not reach default.");
						break;
				}
				return false;
			}
			
		});
		
		expandableList.setOnGroupClickListener(new OnGroupClickListener() {
			
			@Override
			public boolean onGroupClick(ExpandableListView expList, View view,
					int groupPosition, long id) {
				//Toast.makeText(getApplicationContext(), "BOOM! " + groupPosition + " " + id, Toast.LENGTH_SHORT).show();
				//expList.collapseGroup(0);
				return false;
			}
			
		});
	}

	public void setGroupParents() {
		parentItems.add("Medical History");
		parentItems.add("Previous Requests");
		parentItems.add("Referrals");
		parentItems.add("Notes");
	}

	public void setChildData(int patient_id, int encounter_id, String date_encountered) {

		ArrayList<Object> child = new ArrayList<Object>();
		DatabaseAdapter db = new DatabaseAdapter(getApplicationContext());
		
		ArrayList<Encounter> encounterList = db.getPreviousEncounter(patient_id, date_encountered);
		for (int i = 0; i < encounterList.size(); i++) {
			child.add(encounterList.get(i));
		}
		childItems.add(child);
		child = new ArrayList<Object>();
		
		for (int i = 0; i < encounterList.size(); i++) {
			child.add(encounterList.get(i));
		}
		childItems.add(child);
		child = new ArrayList<Object>();
		
		for (int i = 0; i < encounterList.size(); i++) {
			child.add(encounterList.get(i));
		}
		childItems.add(child);
		child = new ArrayList<Object>();
		
		ArrayList<Soap> soapList = db.getDoctorNotes(encounter_id);
		for (int i = 0; i < soapList.size(); i++) {
			child.add(soapList.get(i));
		}
		childItems.add(child);
		
		/*
		// Android
		ArrayList<String> child = new ArrayList<String>();
		child.add("Core");
		child.add("Games");
		childItems.add(child);
		*/

		/*
		// Core Java
		child = new ArrayList<String>();
		child.add("Apache");
		child.add("Applet");
		child.add("AspectJ");
		child.add("Beans");
		child.add("Crypto");
		childItems.add(child);

		// Desktop Java
		child = new ArrayList<String>();
		child.add("Accessibility");
		child.add("AWT");
		child.add("ImageIO");
		child.add("Print");
		childItems.add(child);

		// Enterprise Java
		child = new ArrayList<String>();
		child.add("EJB3");
		child.add("GWT");
		child.add("Hibernate");
		child.add("JSP");
		childItems.add(child);
		*/
	}

	public void setDoctorsNotes(int eid)
	{
		ArrayList<Soap> child = new ArrayList<Soap>();
		DatabaseAdapter db = new DatabaseAdapter(getApplicationContext());
		
		ArrayList<Soap> notelist = db.getDoctorNotes(eid);
		for (int i = 0; i < notelist.size(); i++) {
			child.add(notelist.get(i));
		}
		childItems.add(child);
	}
}