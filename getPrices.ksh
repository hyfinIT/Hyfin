YES_DAT=$(date --date=' 1 days ago' '+%Y-%m-%d')
TODAY_DAT=`date +"20%y-%m-%d"`


open=`python3 PRICEOPEN.py | grep -v "$TODAY_DAT"`
high=`python3 PRICEHIGH.py | grep -v "$TODAY_DAT"`
low=`python3 PRICELOW.py | grep -v "$TODAY_DAT"`
close=`python3 PRICECLOSE.py | grep -v "$TODAY_DAT"`

openPrice=`echo $open | awk '{print $4}'`
highPrice=`echo $high | awk '{print $4}'`
lowPrice=`echo $low | awk '{print $4}'`
closePrice=`echo $close | awk '{print $4}'`

echo "Yest EUR OPEN: $openPrice"
echo "Yest EUR HIGH: $highPrice"
echo "Yest EUR LOW: $lowPrice"
echo "Yest EUR CLOSE $closePrice"




