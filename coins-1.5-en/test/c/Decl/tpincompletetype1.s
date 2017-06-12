 .ident "Coins Compiler version: coins-1.4.3.1 + BackEnd-1.0"
/* JavaCG for target:x86 convention:cygwin */

	.text
	.align	4
	.global	_append
_append:
	pushl	%ebp
	movl	%esp,%ebp
	subl	$8,%esp
	movl	20(%ebp),%eax
	movl	%eax,-8(%ebp)
	movl	$0,-4(%ebp)
	leal	-8(%ebp),%eax
	movl	%eax,16(%ebp)
	leal	-8(%ebp),%edx
	movl	8(%ebp),%ecx
	movl	(%edx),%eax
	movl	%eax,(%ecx)
	movl	4(%edx),%eax
	movl	%eax,4(%ecx)
.L3:
	leave
	ret


	.align	4
	.global	_func
_func:
	pushl	%ebp
	movl	%esp,%ebp
	movl	8(%ebp),%eax
	movl	12(%ebp),%edx
	movl	16(%ebp),%ecx
	movl	(%eax,%ecx,4),%eax
	addl	(%edx,%ecx,4),%eax
	leave
	ret

	.align	1
_string.15:
	.byte	84
	.byte	97
	.byte	110
	.byte	97
	.byte	107
	.byte	97
	.byte	0
	.align	1
_string.16:
	.byte	32
	.byte	0
	.align	1
_string.18:
	.byte	108
	.byte	105
	.byte	115
	.byte	116
	.byte	32
	.byte	37
	.byte	100
	.byte	32
	.byte	10
	.byte	0
	.align	1
_string.22:
	.byte	107
	.byte	32
	.byte	37
	.byte	100
	.byte	10
	.byte	0

	.align	4
	.global	_main
_main:
	pushl	%ebp
	movl	%esp,%ebp
	subl	$64,%esp
	movl	$_string.15,_tanaka
	movl	$184,_tanaka+4
	movl	_tanaka+8,%eax
	cmpl	$0,%eax
	jne	.L10
.L9:
	movl	$_string.16,_tanaka+8
.L10:
	movl	$1,-8(%ebp)
	movl	$0,-4(%ebp)
	pushl	$2
	movl	$8,%eax
	call	__alloca
	leal	-8(%ebp),%edx
	movl	%esp,%ecx
	movl	(%edx),%eax
	movl	%eax,(%ecx)
	movl	4(%edx),%eax
	movl	%eax,4(%ecx)
	leal	-16(%ebp),%eax
	pushl	%eax
	call	_append
	leal	16(%esp),%esp
	pushl	$3
	movl	$8,%eax
	call	__alloca
	leal	-24(%ebp),%edx
	movl	%esp,%ecx
	movl	(%edx),%eax
	movl	%eax,(%ecx)
	movl	4(%edx),%eax
	movl	%eax,4(%ecx)
	leal	-24(%ebp),%eax
	pushl	%eax
	call	_append
	leal	16(%esp),%esp
	pushl	-8(%ebp)
	pushl	$_string.18
	call	_printf
	leal	8(%esp),%esp
	pushl	-16(%ebp)
	pushl	$_string.18
	call	_printf
	leal	8(%esp),%esp
	pushl	-24(%ebp)
	pushl	$_string.18
	call	_printf
	leal	8(%esp),%esp
	movl	$1,_aa+4
	movl	$2,_bb+4
	pushl	$1
	pushl	$_bb
	pushl	$_aa
	call	_func
	leal	12(%esp),%esp
	pushl	%eax
	pushl	$_string.22
	call	_printf
	leal	8(%esp),%esp
	movl	$0,%eax
	leave
	ret

	.comm	_cc,0
	.comm	_tanaka,12
	.comm	_bb,40
	.comm	_t1,12
	.comm	_aa,40
