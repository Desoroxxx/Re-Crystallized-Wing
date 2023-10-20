package dev.redstudio.recrystallizedwing.utils;

import java.nio.file.Path;

import net.minecraftforge.forgespi.locating.IModFile;
import net.minecraftforge.resource.PathPackResources;

/**
 * Taken from <a href="https://github.com/Creators-of-Create/Create/blob/mc1.19/dev/src/main/java/com/simibubi/create/foundation/ModFilePackResources.java">Create</a>
 */
public final class ModFilePackResources extends PathPackResources {

	private final IModFile modFile;
	private final String sourcePath;

	public ModFilePackResources(final String name, final IModFile modFile, final String sourcePath) {
		super(name, modFile.findResource(sourcePath));

		this.modFile = modFile;
		this.sourcePath = sourcePath;
	}

	@Override
	protected Path resolve(final String... paths) {
		final String[] allPaths = new String[paths.length + 1];

		allPaths[0] = sourcePath;

		System.arraycopy(paths, 0, allPaths, 1, paths.length);

		return modFile.findResource(allPaths);
	}
}
