### Result list:ret

### Start symbol:ss

### Final length:len

```

if length of ss <= len:

​	loop for each t in V:

​		if s contains t?

​			s = ss replaces all t with all right hand side in P

​			use ss to generate new sentences

​			add all new generated sentences to ret

​	if ss doesn't contain any symbol in V:

​		add ss to ret

return ret as result 

```
