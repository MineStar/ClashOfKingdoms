package com.bukkit.gemo.ClashOfKingdoms.game;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import org.bukkit.ChunkSnapshot;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.block.Dispenser;
import org.bukkit.block.Furnace;
import org.bukkit.block.NoteBlock;
import org.bukkit.block.Sign;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.bukkit.gemo.ClashOfKingdoms.CoKCore;
import com.bukkit.gemo.utils.BlockUtils;
import com.sk89q.jnbt.ByteArrayTag;
import com.sk89q.jnbt.CompoundTag;
import com.sk89q.jnbt.IntTag;
import com.sk89q.jnbt.ListTag;
import com.sk89q.jnbt.NBTInputStream;
import com.sk89q.jnbt.NBTOutputStream;
import com.sk89q.jnbt.ShortTag;
import com.sk89q.jnbt.StringTag;
import com.sk89q.jnbt.Tag;
import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.blocks.BaseItemStack;
import com.sk89q.worldedit.blocks.ChestBlock;
import com.sk89q.worldedit.blocks.DispenserBlock;
import com.sk89q.worldedit.blocks.FurnaceBlock;
import com.sk89q.worldedit.blocks.MobSpawnerBlock;
import com.sk89q.worldedit.blocks.SignBlock;
import com.sk89q.worldedit.blocks.TileEntityBlock;

public class CoKGameArea implements Serializable
{
	private static final long serialVersionUID = 3137875919486001878L;
	private CoKBlock originBlock = null;
	private Location corner1 = null, corner2 = null;
	
	///////////////////////////////
	//
	// CONSTRUCTOR
	//
	///////////////////////////////	
	public CoKGameArea(Location corner1, Location corner2)
	{
		if(!corner1.getWorld().getName().equalsIgnoreCase(corner2.getWorld().getName()))
			return;		
		this.corner1 = corner1;
		this.corner2 = corner2;
	}
	
	public void writeToFile(String fileName)
	{
		exportSchematic("plugins/ClashOfKingdoms/Areas/" + fileName + ".schematic");
	}
	
	public void exportSchematic(String fileName)
	{
		if(corner1 == null || corner2 == null)
			return;
		
		try
		{
			int minX = Math.min(corner1.getBlockX(), corner2.getBlockX());
			int minY = Math.min(corner1.getBlockY(), corner2.getBlockY());
			int minZ = Math.min(corner1.getBlockZ(), corner2.getBlockZ());
			int maxX = Math.max(corner1.getBlockX(), corner2.getBlockX());
			int maxY = Math.max(corner1.getBlockY(), corner2.getBlockY());
			int maxZ = Math.max(corner1.getBlockZ(), corner2.getBlockZ());
			World world = corner1.getWorld();
			Block block = corner1.getBlock();	
			originBlock = new CoKBlock(world.getName(), minX, minY, minZ);
			
			int width = maxX - minX + 1;
		    int height = maxY - minY + 1;
		    int length = maxZ - minZ + 1;
	
		    if (width > 65535)
		    {
		      throw new Exception("Width of region too large for a .schematic");
		    }
		    if (height > 65535) {
		      throw new Exception("Height of region too large for a .schematic");
		    }
		    if (length > 65535) {
		      throw new Exception("Length of region too large for a .schematic");
		    }
		            
		    byte[] blocks = new byte[width * height * length];
		    byte[] blockData = new byte[width * height * length];
		    List<Tag> tileEntities = new ArrayList<Tag>();
		    
		    ChunkSnapshot snap = null;
		    int actChunkX = 0;
		    int actChunkZ = 0;
		    int oldChunkX = -1;
		    int oldChunkZ = -1;
		    
		    int chunkBlockX = 0;
		    int chunkBlockY = 0;
		    int chunkBlockZ = 0;
		    
		    for (int x = 0; x < width; x++)
		    {
		        for (int y = 0; y < height; y++)
		        {
		          for (int z = 0; z < length; z++)
		          {
		            int index = y * width * length + z * width + x;
		           
		            actChunkX = (int)((x + originBlock.getX()) / 16);
		            actChunkZ = (int)((z + originBlock.getZ()) / 16);
		            
		            if(oldChunkX != actChunkX || oldChunkZ != actChunkZ)
		            {
		            	oldChunkX = actChunkX;
		            	oldChunkZ = actChunkZ;
		            	snap = world.getChunkAt(actChunkX, actChunkZ).getChunkSnapshot(false, false, false);
		            }
		            
		            chunkBlockX = x + originBlock.getX() - (oldChunkX * 16);
		            chunkBlockY = y + originBlock.getY();
		            chunkBlockZ = z + originBlock.getZ() - (oldChunkZ * 16);
		            
		            System.out.println("x - z: " + (oldChunkX * 16) + " / " + y + " / " + (oldChunkZ * 16));
		            System.out.println("block: " + (x + originBlock.getX()) + " / " + (y + originBlock.getY()) + " / " + (z + originBlock.getZ()));
		            System.out.println(chunkBlockX + " / " + chunkBlockY + " / " + chunkBlockZ);
		            System.out.println(" === " + snap.getBlockTypeId(chunkBlockX, chunkBlockY, chunkBlockZ) + ":" + snap.getBlockData(chunkBlockX, chunkBlockY, chunkBlockZ));
		            
		            block = world.getBlockAt(x + originBlock.getX(), y + originBlock.getY(), z + originBlock.getZ());
					blocks[index] = (byte)((short)block.getTypeId());
					blockData[index] = block.getData();
					
					if (BlockUtils.isTileEntity(blocks[index]))
					{
						TileEntityBlock tileEntityBlock = null;
						if(blocks[index] == Material.WALL_SIGN.getId() || blocks[index] == Material.SIGN_POST.getId())
						{
							tileEntityBlock = new SignBlock(blocks[index], blockData[index], ((Sign)block.getState()).getLines());
						}
						else if(blocks[index] == Material.CHEST.getId())
						{
							Inventory inv = ((Chest)block.getState()).getInventory();
							BaseItemStack[] items = new BaseItemStack[inv.getSize()];
							for(int i = 0; i < inv.getSize(); i++)
							{
								items[i] = new BaseItemStack(inv.getItem(i).getTypeId(), inv.getItem(i).getAmount(), inv.getItem(i).getDurability());
							}
							tileEntityBlock = new ChestBlock(blockData[index], items);
						}
						else if(blocks[index] == Material.NOTE_BLOCK.getId())
						{
							tileEntityBlock = new com.sk89q.worldedit.blocks.NoteBlock(blockData[index], ((NoteBlock)block.getState()).getRawNote());
						}
						else if(blocks[index] == Material.MOB_SPAWNER.getId())
						{
							tileEntityBlock = new MobSpawnerBlock(((CreatureSpawner)block.getState()).getCreatureTypeId());
						}
						else if(blocks[index] == Material.FURNACE.getId() || blocks[index] == Material.BURNING_FURNACE.getId())
						{
							Inventory inv = ((Furnace)block.getState()).getInventory();
							BaseItemStack[] items = new BaseItemStack[inv.getSize()];
							items[0] = new BaseItemStack(inv.getItem(0).getTypeId(), inv.getItem(0).getAmount(), inv.getItem(0).getDurability());
							items[1] = new BaseItemStack(inv.getItem(1).getTypeId(), inv.getItem(1).getAmount(), inv.getItem(1).getDurability());
							tileEntityBlock = new FurnaceBlock(blocks[index], blockData[index], items);
						}
						else if(blocks[index] == Material.DISPENSER.getId())
						{
							Inventory inv = ((Dispenser)block.getState()).getInventory();
							BaseItemStack[] items = new BaseItemStack[inv.getSize()];
							for(int i = 0; i < inv.getSize(); i++)
							{
								items[i] = new BaseItemStack(inv.getItem(i).getTypeId(), inv.getItem(i).getAmount(), inv.getItem(i).getDurability());
							}
							tileEntityBlock = new DispenserBlock(blockData[index], items);
						}
						
						if(tileEntityBlock != null)
						{
							Map<String, Tag> values = tileEntityBlock.toTileEntityNBT();
				            if (values != null)
				            {
				              values.put("id", new StringTag("id", tileEntityBlock.getTileEntityID()));
				              values.put("x", new IntTag("x", x + originBlock.getX()));
				              values.put("y", new IntTag("y", y + originBlock.getY()));
				              values.put("z", new IntTag("z", z + originBlock.getZ()));
				              CompoundTag tileEntityTag = new CompoundTag("TileEntity", values);
				              tileEntities.add(tileEntityTag);
				            }
						}
			          }					
		          }
		        }
		    }
	
		    long startT = System.currentTimeMillis();
		    HashMap<String, Tag> schematic = new HashMap<String, Tag>();
		    schematic.put("Width", new ShortTag("Width", (short)width));
		    schematic.put("Length", new ShortTag("Length", (short)length));
		    schematic.put("Height", new ShortTag("Height", (short)height));
		    schematic.put("OriginX", new IntTag("OriginX", originBlock.getX()));
		    schematic.put("OriginY", new IntTag("OriginY", originBlock.getY()));
		    schematic.put("OriginZ", new IntTag("OriginZ", originBlock.getZ()));
		    schematic.put("OriginWorld", new StringTag("OriginWorld", originBlock.getWorldName()));
     	    
		    schematic.put("Blocks", new ByteArrayTag("Blocks", blocks));
		    schematic.put("Data", new ByteArrayTag("Data", blockData));
		    schematic.put("TileEntities", new ListTag("TileEntities", CompoundTag.class, tileEntities));
		    
		    CompoundTag schematicTag = new CompoundTag("Schematic", schematic);
		    NBTOutputStream stream = new NBTOutputStream(new FileOutputStream(fileName));
		    stream.writeTag(schematicTag);
		    stream.close();
		    
		    long endT = System.currentTimeMillis();
		    System.out.println("Time for " + blocks.length + " blocks: " + (endT - startT) + " ms");
		}
		catch(Exception e)
		{
			e.printStackTrace();  
		}
	}
	
	private static Tag getChildTag(Map<String, Tag> items, String key, Class<? extends Tag> expected) throws Exception 
	{
		if (!items.containsKey(key)) 
		{
			throw new Exception("Schematic file is missing a \"" + key
					+ "\" tag");
		}
		Tag tag = (Tag) items.get(key);
		if (!expected.isInstance(tag)) 
		{
			throw new Exception(key + " tag is not of tag type "
					+ expected.getName());
		}
		return tag;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static int reloadArea(String fileName)
	{
		try
		{			
			// MAKE DIRS
			File dir = new File("plugins/ClashOfKingdoms/Areas/" + fileName + ".schematic");
		
			// IS FILE THERE?
			if(!dir.exists())
			{
				return -1;
			}
		
			FileInputStream stream = new FileInputStream("plugins/ClashOfKingdoms/Areas/" + fileName + ".schematic");
		    NBTInputStream nbtStream = new NBTInputStream(new GZIPInputStream(stream));

		    CompoundTag schematicTag = (CompoundTag)nbtStream.readTag();	
		    Map<String, Tag> schematic = schematicTag.getValue();

		    short width = ((Short)getChildTag(schematic, "Width", ShortTag.class).getValue()).shortValue();
		    short length = ((Short)getChildTag(schematic, "Length", ShortTag.class).getValue()).shortValue();
		    short height = ((Short)getChildTag(schematic, "Height", ShortTag.class).getValue()).shortValue();
		    
		    int originX = ((Integer)getChildTag(schematic, "OriginX", IntTag.class).getValue()).intValue();
		    int originY = ((Integer)getChildTag(schematic, "OriginY", IntTag.class).getValue()).intValue();
		    int originZ = ((Integer)getChildTag(schematic, "OriginZ", IntTag.class).getValue()).intValue();
		    String originWorld = (String)getChildTag(schematic, "OriginWorld", StringTag.class).getValue();
		    CoKBlock originBlock = new CoKBlock(originWorld, originX, originY, originZ);
		    
		    World world = CoKCore.server.getWorld(originBlock.getWorldName());
   
		    byte[] blocks = (byte[])(byte[])getChildTag(schematic, "Blocks", ByteArrayTag.class).getValue();
		    byte[] blockData = (byte[])(byte[])getChildTag(schematic, "Data", ByteArrayTag.class).getValue();
			
		    ArrayList<CoKBlock> queuedBlocks = new ArrayList<CoKBlock>();
		    int index = 0;
		    for (int x = 0; x < width; x++) 
		    {
		        for (int y = 0; y < height; y++) 
		        {
		          for (int z = 0; z < length; z++) 
		          {
		        	  index = y * width * length + z * width + x;
		        	  if(!BlockUtils.isComplexBlock(blocks[index]))
		        	  {
		        		  world.getBlockAt(x + originX, y + originY, z + originZ).setTypeIdAndData(blocks[index], blockData[index], true);
		        	  }
		        	  else
		        	  {
		        		  queuedBlocks.add(new CoKBlock(blocks[index], blockData[index], originWorld, x + originX, y + originY, z + originZ));
		        	  }
		          }
		        }
		    } 
		    			
			// HANDLE QUEUED BLOCKS
			for(int i = 0; i < queuedBlocks.size(); i++)
			{
				world.getBlockAt(queuedBlocks.get(i).getX(), queuedBlocks.get(i).getY(), queuedBlocks.get(i).getZ()).setTypeIdAndData(queuedBlocks.get(i).getId(), queuedBlocks.get(i).getSubId(), true);
			}	
			
			List<Tag> tileEntities = ((ListTag)getChildTag(schematic, "TileEntities", ListTag.class)).getValue();
			Map tileEntitiesMap = new HashMap();
			for (Tag tag : tileEntities) 
			{
				if ((tag instanceof CompoundTag)) 
				{
					CompoundTag t = (CompoundTag) tag;

					int x = 0;
					int y = 0;
					int z = 0;

					Map values = new HashMap();

					for (Map.Entry entry : t.getValue().entrySet()) 
					{
						if (((String) entry.getKey()).equals("x")) 
						{
							if ((entry.getValue() instanceof IntTag))
								x = ((IntTag) entry.getValue()).getValue().intValue();
						} 
						else if (((String) entry.getKey()).equals("y")) 
						{
							if ((entry.getValue() instanceof IntTag))
								y = ((IntTag) entry.getValue()).getValue().intValue();
						} 
						else if ((((String) entry.getKey()).equals("z")) && ((entry.getValue() instanceof IntTag))) 
						{
							z = ((IntTag) entry.getValue()).getValue().intValue();
						}
						values.put(entry.getKey(), entry.getValue());
					}

					BlockVector vec = new BlockVector(x, y, z);
					tileEntitiesMap.put(vec, values);
				}
			}

			for (int x = 0; x < width; x++) 
			{
				for (int y = 0; y < height; y++) 
				{
					for (int z = 0; z < length; z++) 
					{
						index = y * width * length + z * width + x;
						BlockVector pt = new BlockVector(x + originX, y + originY, z + originZ);
						BaseBlock block;
						if ((blocks[index] == Material.WALL_SIGN.getId()) || (blocks[index] == Material.SIGN_POST.getId())) 
						{
							block = new SignBlock(blocks[index],blockData[index]);
						}
						else 
						{
							if (blocks[index] == Material.CHEST.getId()) 
							{
								block = new ChestBlock(blockData[index]);
							} 
							else 
							{
								if ((blocks[index] == Material.FURNACE.getId())|| (blocks[index] == Material.BURNING_FURNACE.getId())) 
								{
									block = new FurnaceBlock(blocks[index],blockData[index]);
								} 
								else 
								{
									if (blocks[index] == Material.DISPENSER.getId()) 
									{
										block = new DispenserBlock(blockData[index]);
									} 
									else 
									{
										if (blocks[index] == Material.MOB_SPAWNER.getId()) 
										{
											block = new MobSpawnerBlock(blockData[index]);
										} 
										else 
										{
											if (blocks[index] == Material.NOTE_BLOCK.getId())
												block = new com.sk89q.worldedit.blocks.NoteBlock(blockData[index]);
											else
												block = new BaseBlock(blocks[index], blockData[index]);
										}
									}
								}
							}
						}
						if (((block instanceof TileEntityBlock)) && (tileEntitiesMap.containsKey(pt)))
						{
							Block thisBlock =  world.getBlockAt(x + originX, y + originY, z + originZ);
							((TileEntityBlock) block).fromTileEntityNBT((Map) tileEntitiesMap.get(pt));
							if(block instanceof SignBlock)
							{
								for(int i = 0; i < ((SignBlock)block).getText().length; i++)								
									((Sign)thisBlock.getState()).setLine(i,  ((SignBlock)block).getText()[i]);
							}
							else if(block instanceof ChestBlock)
							{
								for(int i = 0; i < ((ChestBlock)block).getItems().length; i++)	
								{
									if(((ChestBlock)block).getItems()[i] == null)											
									{
										continue;
									}
									if(((ChestBlock)block).getItems()[i].getType() > 0)											
									{
										ItemStack item = new ItemStack(((ChestBlock)block).getItems()[i].getType(), ((ChestBlock)block).getItems()[i].getAmount());
										if(((ChestBlock)block).getItems()[i].getDamage() > 0)
											item.setDurability(((ChestBlock)block).getItems()[i].getDamage());
										if(item != null)
											((Chest)thisBlock.getState()).getInventory().setItem(i, item);
									}
								}
							}
							else if(block instanceof DispenserBlock)
							{
								for(int i = 0; i < ((DispenserBlock)block).getItems().length; i++)	
								{
									if(((DispenserBlock)block).getItems()[i] == null)											
									{
										continue;
									}
									if(((DispenserBlock)block).getItems()[i].getType() > 0)											
									{
											ItemStack item = new ItemStack(((DispenserBlock)block).getItems()[i].getType(), ((DispenserBlock)block).getItems()[i].getAmount());
											if(((DispenserBlock)block).getItems()[i].getDamage() > 0)
												item.setDurability(((DispenserBlock)block).getItems()[i].getDamage());
											if(item != null)
												((Dispenser)thisBlock.getState()).getInventory().setItem(i, item);
									}
								}
							}
							else if(block instanceof FurnaceBlock)
							{
								for(int i = 0; i < ((FurnaceBlock)block).getItems().length; i++)	
								{
									if(((FurnaceBlock)block).getItems()[i] == null)											
									{
										continue;
									}
									if(((FurnaceBlock)block).getItems()[i].getType() > 0)											
									{
											ItemStack item = new ItemStack(((FurnaceBlock)block).getItems()[i].getType(), ((FurnaceBlock)block).getItems()[i].getAmount());
										if(((FurnaceBlock)block).getItems()[i].getDamage() > 0)
											item.setDurability(((FurnaceBlock)block).getItems()[i].getDamage());
										if(item != null)
											((Furnace)thisBlock.getState()).getInventory().setItem(i, item);
									}
								}
							}
							else if(block instanceof com.sk89q.worldedit.blocks.NoteBlock)
							{
								((NoteBlock)thisBlock.getState()).setRawNote(((com.sk89q.worldedit.blocks.NoteBlock)block).getNote());
							}
							else if(block instanceof MobSpawnerBlock)
							{
								((CreatureSpawner)thisBlock.getState()).setCreatureTypeId(((MobSpawnerBlock)block).getMobType());
							}
						}
					}
				}
			}
			
		    nbtStream.close();
		    stream.close();
			return blocks.length;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			CoKCore.printInConsole("Error while loading gamearea: " + fileName);
			return 0;
		}		
	}
}
