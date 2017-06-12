 .ident "Coins Compiler version: coins-1.4.3.1 + BackEnd-1.0"
/* JavaCG for target:x86 convention:cygwin */
	.text
	.align	1
_string.3:
	.byte	37
	.byte	108
	.byte	108
	.byte	100
	.byte	32
	.byte	37
	.byte	104
	.byte	100
	.byte	32
	.byte	37
	.byte	100
	.byte	32
	.byte	37
	.byte	100
	.byte	32
	.byte	37
	.byte	100
	.byte	32
	.byte	10
	.byte	0

	.align	4
	.global	_main
_main:
	pushl	%ebp
	movl	%esp,%ebp
	movl	$2,%eax
	movl	$0,%edx
	movl	%eax,_ll
	movl	%edx,_ll+4
	movl	_ll,%eax
	movl	_ll+4,%edx
	movw	%ax,%ax
	movw	%ax,_s
	movl	_ll,%eax
	movl	_ll+4,%edx
	movl	%eax,%eax
	movl	$3,_a(,%eax,4)
	movswl	_s,%eax
	movl	_a(,%eax,4),%eax
	movl	%eax,_a+12
	movl	_ll,%eax
	movl	_ll+4,%edx
	movl	%eax,%eax
	movl	_a(,%eax,4),%eax
	movl	%eax,_a+16
	pushl	_a+16
	pushl	_a+12
	pushl	_a+8
	movswl	_s,%eax
	pushl	%eax
	pushl	_ll+4
	pushl	_ll
	pushl	$_string.3
	call	_printf
	leal	28(%esp),%esp
	movl	$0,%eax
	leave
	ret

	.comm	_l,4
	.comm	_s,2
	.comm	_ll,8
	.comm	_a,40
