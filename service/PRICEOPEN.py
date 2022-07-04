import yfinance as yf

def price(ticker, period='1d',columns=['Open']):
    '''
    Returns a DataFrame of prices for ticker from Yahoo Finance API
    '''
    obj = yf.Ticker(ticker)
    return obj.history(period=period)[columns]


test = price('GBPUSD=X',period='2d')
test
print (test)

