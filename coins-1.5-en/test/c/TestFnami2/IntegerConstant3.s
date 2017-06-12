 .ident "Coins Compiler version: coins-1.4.3.1 + BackEnd-1.0"
/* JavaCG for target:x86 convention:cygwin */
	.text
	.align	1
_string.7:
	.byte	37
	.byte	48
	.byte	56
	.byte	88
	.byte	32
	.byte	37
	.byte	48
	.byte	56
	.byte	88
	.byte	32
	.byte	37
	.byte	48
	.byte	56
	.byte	88
	.byte	10
	.byte	0

	.align	4
	.global	_fi
_fi:
	pushl	%ebp
	movl	%esp,%ebp
	movl	8(%ebp),%ecx
	movl	$-3,%eax
	sarl	%cl,%eax
	pushl	%eax
	movl	$-2,%eax
	sarl	%cl,%eax
	pushl	%eax
	movl	$-1,%eax
	sarl	%cl,%eax
	pushl	%eax
	pushl	$_string.7
	call	_printf
	leal	16(%esp),%esp
.L3:
	leave
	ret

	.align	1
_string.9:
	.byte	37
	.byte	48
	.byte	56
	.byte	88
	.byte	32
	.byte	37
	.byte	48
	.byte	56
	.byte	88
	.byte	32
	.byte	37
	.byte	48
	.byte	56
	.byte	88
	.byte	32
	.byte	37
	.byte	48
	.byte	56
	.byte	88
	.byte	32
	.byte	37
	.byte	48
	.byte	56
	.byte	88
	.byte	10
	.byte	0

	.align	4
	.global	_fui
_fui:
	pushl	%ebp
	movl	%esp,%ebp
	movl	8(%ebp),%ecx
	movl	$-1879048192,%eax
	shrl	%cl,%eax
	pushl	%eax
	movl	$-3,%eax
	shrl	%cl,%eax
	pushl	%eax
	movl	$-2147483648,%eax
	shrl	%cl,%eax
	pushl	%eax
	movl	$-2,%eax
	shrl	%cl,%eax
	pushl	%eax
	movl	$-1,%eax
	shrl	%cl,%eax
	pushl	%eax
	pushl	$_string.9
	call	_printf
	leal	24(%esp),%esp
.L6:
	leave
	ret

	.align	1
_string.11:
	.byte	37
	.byte	48
	.byte	56
	.byte	108
	.byte	88
	.byte	32
	.byte	37
	.byte	48
	.byte	56
	.byte	108
	.byte	88
	.byte	32
	.byte	37
	.byte	48
	.byte	56
	.byte	108
	.byte	88
	.byte	10
	.byte	0

	.align	4
	.global	_fl
_fl:
	pushl	%ebp
	movl	%esp,%ebp
	movl	8(%ebp),%ecx
	movl	$-3,%eax
	sarl	%cl,%eax
	pushl	%eax
	movl	$-2,%eax
	sarl	%cl,%eax
	pushl	%eax
	movl	$-1,%eax
	sarl	%cl,%eax
	pushl	%eax
	pushl	$_string.11
	call	_printf
	leal	16(%esp),%esp
.L9:
	leave
	ret

	.align	1
_string.13:
	.byte	37
	.byte	48
	.byte	56
	.byte	108
	.byte	88
	.byte	32
	.byte	37
	.byte	48
	.byte	56
	.byte	108
	.byte	88
	.byte	32
	.byte	37
	.byte	48
	.byte	56
	.byte	108
	.byte	88
	.byte	32
	.byte	37
	.byte	48
	.byte	56
	.byte	108
	.byte	88
	.byte	32
	.byte	37
	.byte	48
	.byte	56
	.byte	108
	.byte	88
	.byte	10
	.byte	0

	.align	4
	.global	_ful
_ful:
	pushl	%ebp
	movl	%esp,%ebp
	movl	8(%ebp),%ecx
	movl	$-1879048192,%eax
	shrl	%cl,%eax
	pushl	%eax
	movl	$-3,%eax
	shrl	%cl,%eax
	pushl	%eax
	movl	$-2147483648,%eax
	shrl	%cl,%eax
	pushl	%eax
	movl	$-2,%eax
	shrl	%cl,%eax
	pushl	%eax
	movl	$-1,%eax
	shrl	%cl,%eax
	pushl	%eax
	pushl	$_string.13
	call	_printf
	leal	24(%esp),%esp
.L12:
	leave
	ret


	.align	4
	.global	_main
_main:
	pushl	%ebp
	movl	%esp,%ebp
	movl	$5,%eax
	pushl	$-1
	pushl	$-1
	pushl	$-1
	pushl	$_string.7
	call	_printf
	leal	16(%esp),%esp
	pushl	$-1
	pushl	$-1
	pushl	$-1
	pushl	$_string.7
	call	_printf
	leal	16(%esp),%esp
	pushl	$5
	call	_fi
	leal	4(%esp),%esp
	movl	$5,%eax
	pushl	$75497472
	pushl	$134217727
	pushl	$67108864
	pushl	$134217727
	pushl	$134217727
	pushl	$_string.9
	call	_printf
	leal	24(%esp),%esp
	pushl	$75497472
	pushl	$134217727
	pushl	$67108864
	pushl	$134217727
	pushl	$134217727
	pushl	$_string.9
	call	_printf
	leal	24(%esp),%esp
	pushl	$5
	call	_fui
	leal	4(%esp),%esp
	movl	$5,%eax
	pushl	$-1
	pushl	$-1
	pushl	$-1
	pushl	$_string.11
	call	_printf
	leal	16(%esp),%esp
	pushl	$-1
	pushl	$-1
	pushl	$-1
	pushl	$_string.11
	call	_printf
	leal	16(%esp),%esp
	pushl	$5
	call	_fl
	leal	4(%esp),%esp
	movl	$5,%eax
	pushl	$75497472
	pushl	$134217727
	pushl	$67108864
	pushl	$134217727
	pushl	$134217727
	pushl	$_string.13
	call	_printf
	leal	24(%esp),%esp
	pushl	$75497472
	pushl	$134217727
	pushl	$67108864
	pushl	$134217727
	pushl	$134217727
	pushl	$_string.13
	call	_printf
	leal	24(%esp),%esp
	pushl	$5
	call	_ful
	leal	4(%esp),%esp
	movl	$0,%eax
	leave
	ret

