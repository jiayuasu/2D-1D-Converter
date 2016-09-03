# 2D-1D-Converter

##Introduction

This tool will downgrade 2 dimensional data (like spatial longitude and latitude) to 1 dimensional data by using Hilbert Curve. 

## Format

It will use arg[0] and arg[1] in the main function as the input path and output path. Each two dimensional data pair will be matched to a hilbert curve point ID. This ID will be attached to the end of each input line.

## Features

### Support decimal
It can convert two dimensional decimal to one dimensional ID. The resolution is controlled by 'gridResolution'.


### No memory limitation
This code will process the input file line by line and output the processed results in batches. By using this code, your input and output data file size can go beyond the memory limitation. The output batch size is controlled by 'statusInterval'.

##Usage:

`
java -jar hilbertMarker.jar input output
`
