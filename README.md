# 2D-1D-Converter

## Introduction

This tool will reduce 2 dimensions (like spatial longitude and latitude) to 1 dimension by using Hilbert Curve. 

## Format

It will use arg[0] and arg[1] in the main function as the input path and output path. Each two dimensional data pair will be matched to a hilbert curve point ID. This ID will be attached to the end of each input line. arg[2] and arg[3] specify the longitude and latitude offset in each row. arg[4] (true/false)indicates whether you want to inplace longitude and latitude replacement or just attach hilbert curve value to the end of each line.

## Features

### Support decimal
It can convert two dimensional decimal to one dimensional ID. The resolution is controlled by 'gridResolution'.


### No memory limitation
This code will process the input file line by line and output the processed results in batches. By using this code, your input and output data file size can go beyond the memory limitation. The output batch size is controlled by 'statusInterval'.

## Usage:

`
java -jar hilbertMarker.jar inputPath outputPath 0 1 true
`
