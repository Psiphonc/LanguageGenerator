### Pseudocode of the main algorithm

| Variable |            meaning            |
| :------: | :---------------------------: |
|    ss    |         start symbol          |
|  len  | specific length given by user |
|   ret    |          result list          |
|   LHS    |          left hand side list in P         |
|   rhs    |          right hand side string in P         |

```

if length of ss <= len:

	loop for each t in LHS:

		if ss contains t:

			s = ss replaces t with all rhs in P

			use s to generate new sentences

			add all new generated sentences to ret

	if ss doesn't contain any symbol in V:

		add ss to ret

return ret as result 

```

### flow diagram
(may not present in github)
```flow
st=>start: Start
ed=>end: return ret
shortercheck=>condition: ss.length <= length?
getAllV=>condition: get first(next) v in V
ifsctnv=>condition: ss contains v? 
replc=>operation: replaces all t with all rhs in P
gns=>operation: use the sentence to generate new sentences
ans=>operation: add all new generated sentences to ret
ifnctnv=>condition: ss doesn't contain any symbol in V?
2ret=>operation: add ss to ret

st->shortercheck
shortercheck(no)->ed
shortercheck(yes,right)->getAllV
getAllV(yes)->ifsctnv
ifsctnv(yes)->replc->gns->ans->getAllV
getAllV(no)->ifnctnv
ifnctnv(yes,right)->2ret->ed
ifnctnv(no)->ed



```