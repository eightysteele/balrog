# Overview

The `tools` directory contains tools for tiling.

# gdal2tiles.py

The `gdal2tiles.py` Python script was modified from [the original](http://code.google.com/p/maptiler/source/browse/trunk/maptiler/gdal2tiles.py?r=17) to output a bunch of little files into a single directory, rather than into a nested in subfolder hierarchy. In particular, `ROOTDIR/x_y_z.png` instead of `ROOTDIR/x/y/z.png`. 

For this thing to work, we first need to create a [GDAL Virtual Format](http://www.gdal.org/gdal_vrttut.html) file based on our input raster file. We can then pass that into `gdal2tiles.py` and we are SET.

Here's an example using the example `c41078a1.tif` file in this directory:


```bash
$ gdal_translate -of vrt -expand rgba /local/path/to/raster.tif raster.vrt
$ ./gdal2tiles.py raster.vrt

```

This will create a directory named `raster` with all of the tiles. To control the zoom levels to render, and for other options, look at `./gdal2tiles.py --help`. 
