# gdal2tiles.py

The `gdal2tiles.py` Python script was modified from [the original](http://code.google.com/p/maptiler/source/browse/trunk/maptiler/gdal2tiles.py?r=17) to output a bunch of little files into a single directory, rather than into a nested in subfolder hierarchy. In particular, the script now creates files like `ROOTDIR/x_y_z.png` instead of like `ROOTDIR/x/y/z.png`. 

For this thing to work, we first need to create a [GDAL Virtual Format](http://www.gdal.org/gdal_vrttut.html) file based on our input raster file. We can then pass that into `gdal2tiles.py` and we are SET.

Here's an example using the example `c41078a1.tif` file in this directory:


```bash
# Create the VRT file using gdal_translate:
$ gdal_translate -of vrt -expand rgba /local/path/to/raster.tif raster.vrt

# Create the tiles!
$ ./gdal2tiles.py raster.vrt

```

This will create a directory named `raster` with all of the tiles in it. To control the specific zoom levels to render, and for other options, look at `./gdal2tiles.py --help` for details. 
