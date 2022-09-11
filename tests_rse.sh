for (( i=0; i<=70; ++i));
do
    echo -n $i \-\>' ';
    java lang/rse/RseParser data/rse/parser-tests/test$i.rse;
done