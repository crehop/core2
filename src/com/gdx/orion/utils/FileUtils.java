package com.gdx.orion.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.gdx.orion.entities.voxel.VoxelType;

public class FileUtils 
{

	public static void serializeVoxel2DArray(String filename, VoxelType[][] voxels)
	{
		try
		{
			FileOutputStream fout = new FileOutputStream(System.getProperty("user.dir") + "/res/" + filename + ".ser");
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			oos.writeObject(voxels);
			oos.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static VoxelType[][] deserializeVoxel2DArray(String filename)
	{
		try 
		{
			FileInputStream fin = new FileInputStream(System.getProperty("user.dir") + "/res/" + filename + ".ser");
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
	
	public static String[] displayLoadableVoxelFileNames()
	{
		File file = new File(System.getProperty("user.dir") + "/res");
		File[] files = file.listFiles();
		
		String[] names = new String[files.length];
		
		for (int x = 0; x < files.length; x++)
		{
			names[x] = files[x].getName();
		}
		
		return names;
	}
	
}
