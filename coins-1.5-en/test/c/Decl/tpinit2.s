 .ident "Coins Compiler version: coins-1.4.3.1 + BackEnd-1.0"
/* JavaCG for target:x86 convention:cygwin */
	.text
	.align	1
_string.6:
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
	subl	$12,%esp
	movb	$1,-4(%ebp)
	movb	$2,-3(%ebp)
	movb	$3,-2(%ebp)
	movb	$4,-1(%ebp)
	movb	$5,-8(%ebp)
	movb	$6,-7(%ebp)
	movb	$7,-6(%ebp)
	movb	$8,-5(%ebp)
	movb	$9,-12(%ebp)
	movb	$10,-11(%ebp)
	movb	$11,-10(%ebp)
	movb	$12,-9(%ebp)
	movsbl	-10(%ebp),%eax
	pushl	%eax
	movsbl	-7(%ebp),%eax
	pushl	%eax
	movsbl	-1(%ebp),%eax
	pushl	%eax
	pushl	$_string.6
	call	_printf
	leal	16(%esp),%esp
	movl	$0,%eax
	leave
	ret

