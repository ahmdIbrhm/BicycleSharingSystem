#!/bin/bash
# start=`date +%s%3N`
curl -X POST \
     -H Content-Type:text/n3 \
     -T $1 \
     -G http://localhost:3030/test/data \
     -d default
# end=`date +%s%3N`
# runtime=$((end-start))
# echo "Loading time: $runtime"
rm -f $1
echo "Finished script!"




