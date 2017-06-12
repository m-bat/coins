 .ident "Coins Compiler version: coins-1.4.3.1 + BackEnd-1.0"
/* JavaCG for target:x86 convention:cygwin */
	.text
	.align	1
_string.3:
	.byte	37
	.byte	100
	.byte	44
	.byte	37
	.byte	100
	.byte	44
	.byte	37
	.byte	100
	.byte	44
	.byte	37
	.byte	100
	.byte	44
	.byte	37
	.byte	100
	.byte	44
	.byte	37
	.byte	100
	.byte	0

	.align	4
	.global	_main
_main:
	pushl	%ebp
	movl	%esp,%ebp
	pushl	$260
	pushl	$277380
	pushl	$66816
	pushl	$35964
	pushl	$480
	pushl	$976
	pushl	$_string.3
	call	_printf
	leal	28(%esp),%esp
	movl	$0,%eax
	leave
	ret

	.comm	_charArray1,20
	.comm	_arrayArrays,66816
	.comm	_basicType1,36
	.comm	_nestedStruct1,52
	.comm	_userInfo1,16
	.comm	_arrayBasicType,35964
	.comm	_arrayCharArray,277380
	.comm	_arrayUserInfo,976
	.comm	_arrayNestedStruct,260
	.comm	_arrays1,232
	.comm	_arrayClass,480
	.comm	_class1,20
