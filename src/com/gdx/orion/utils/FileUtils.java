package com.gdx.orion.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.gdx.orion.entities.voxel.VoxelType;

public class FileUtils 
{

	public static void serializeObject(String filename, Object object)
	{
		try
		{
			FileOutputStream fout = new FileOutputStream(System.getProperty("user.dir") + "/res/voxelarrays/" + filename + ".ser");
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			oos.writeObject(object);
			oos.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static VoxelType[][] deserializeObject(String filename)
	{
		try 
		{
			FileInputStream fin = new FileInputStream(System.getProperty("user.dir") + "/res/voxelarrays/" + filename + ".ser");
			ObjectInputStream ois = new ObjectInputStream(fin);
			VoxelType[][] voxels = (VoxelType[][]) ois.readObject();
			ois.close();
			return voxels;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	public static String[] displayLoadableObjectFileNames()
	{
		File file = new File(System.getProperty("user.dir") + "/res/voxelarrays");
		File[] files = file.listFiles();
		
		try 
		{
			if (files.length != 0) {
		
			String[] names = new String[files.length];
		
			for (int x = 0; x < files.length; x++)
			{
				names[x] = files[x].getName();
			}
		
			return names;
			}
		}
		catch (NullPointerException ex) {}
		return null;
	}
	
}
