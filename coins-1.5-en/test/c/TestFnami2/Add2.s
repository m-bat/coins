 .ident "Coins Compiler version: coins-1.4.3.1 + BackEnd-1.0"
/* JavaCG for target:x86 convention:cygwin */

	.text
	.align	4
	.global	_f0
_f0:
	pushl	%ebp
	movl	%esp,%ebp
	movl	8(%ebp),%ecx
	leal	-1(%ecx),%eax
	pushl	%eax
	leal	-11(%ecx),%eax
	pushl	%eax
	leal	2147483647(%ecx),%eax
	pushl	%eax
	leal	32769(%ecx),%eax
	pushl	%eax
	leal	32768(%ecx),%eax
	pushl	%eax
	leal	32767(%ecx),%eax
	pushl	%eax
	leal	4097(%ecx),%eax
	pushl	%eax
	leal	4096(%ecx),%eax
	pushl	%eax
	leal	4095(%ecx),%eax
	pushl	%eax
	leal	129(%ecx),%eax
	pushl	%eax
	leal	128(%ecx),%eax
	pushl	%eax
	leal	127(%ecx),%eax
	pushl	%eax
	leal	3(%ecx),%eax
	pushl	%eax
	leal	2(%ecx),%eax
	pushl	%eax
	leal	1(%ecx),%eax
	pushl	%eax
	pushl	%ecx
	pushl	$_s16
	call	_printf
	leal	68(%esp),%esp
.L3:
	leave
	ret


	.align	4
	.global	_f1
_f1:
	pushl	%ebp
	movl	%esp,%ebp
	movl	8(%ebp),%ecx
	leal	-1(%ecx),%eax
	pushl	%eax
	leal	-11(%ecx),%eax
	pushl	%eax
	leal	2147483647(%ecx),%eax
	pushl	%eax
	leal	32769(%ecx),%eax
	pushl	%eax
	leal	32768(%ecx),%eax
	pushl	%eax
	leal	32767(%ecx),%eax
	pushl	%eax
	leal	4097(%ecx),%eax
	pushl	%eax
	leal	4096(%ecx),%eax
	pushl	%eax
	leal	4095(%ecx),%eax
	pushl	%eax
	leal	129(%ecx),%eax
	pushl	%eax
	leal	128(%ecx),%eax
	pushl	%eax
	leal	127(%ecx),%eax
	pushl	%eax
	leal	3(%ecx),%eax
	pushl	%eax
	leal	2(%ecx),%eax
	pushl	%eax
	leal	1(%ecx),%eax
	pushl	%eax
	pushl	%ecx
	pushl	$_s16
	call	_printf
	leal	68(%esp),%esp
.L6:
	leave
	ret


	.align	4
	.global	_op
_op:
	pushl	%ebp
	movl	%esp,%ebp
	movl	8(%ebp),%ecx
	movl	12(%ebp),%eax
	leal	(%ecx,%eax),%eax
	leave
	ret


	.align	4
	.global	_f2
_f2:
	pushl	%ebp
	movl	%esp,%ebp
	subl	$52,%esp
	pushl	%ebx
	pushl	%esi
	pushl	%edi
	movl	8(%ebp),%eax
	movl	%eax,-20(%ebp)
	pushl	$0
	pushl	-20(%ebp)
	call	_op
	movl	%eax,-52(%ebp)
	leal	8(%esp),%esp
	pushl	$1
	pushl	-20(%ebp)
	call	_op
	movl	%eax,-48(%ebp)
	leal	8(%esp),%esp
	pushl	$2
	pushl	-20(%ebp)
	call	_op
	movl	%eax,-44(%ebp)
	leal	8(%esp),%esp
	pushl	$3
	pushl	-20(%ebp)
	call	_op
	movl	%eax,-40(%ebp)
	leal	8(%esp),%esp
	pushl	$127
	pushl	-20(%ebp)
	call	_op
	movl	%eax,-36(%ebp)
	leal	8(%esp),%esp
	pushl	$128
	pushl	-20(%ebp)
	call	_op
	movl	%eax,-32(%ebp)
	leal	8(%esp),%esp
	pushl	$129
	pushl	-20(%ebp)
	call	_op
	movl	%eax,-28(%ebp)
	leal	8(%esp),%esp
	pushl	$4095
	pushl	-20(%ebp)
	call	_op
	movl	%eax,-24(%ebp)
	leal	8(%esp),%esp
	pushl	$4096
	pushl	-20(%ebp)
	call	_op
	movl	%eax,-16(%ebp)
	leal	8(%esp),%esp
	pushl	$4097
	pushl	-20(%ebp)
	call	_op
	movl	%eax,-12(%ebp)
	leal	8(%esp),%esp
	pushl	$32767
	pushl	-20(%ebp)
	call	_op
	movl	%eax,-8(%ebp)
	leal	8(%esp),%esp
	pushl	$32768
	pushl	-20(%ebp)
	call	_op
	movl	%eax,-4(%ebp)
	leal	8(%esp),%esp
	pushl	$32769
	pushl	-20(%ebp)
	call	_op
	movl	%eax,%edi
	leal	8(%esp),%esp
	pushl	$2147483637
	pushl	-20(%ebp)
	call	_op
	movl	%eax,%esi
	leal	8(%esp),%esp
	pushl	$-11
	pushl	-20(%ebp)
	call	_op
	movl	%eax,%ebx
	leal	8(%esp),%esp
	pushl	$-1
	pushl	-20(%ebp)
	call	_op
	leal	8(%esp),%esp
	pushl	%eax
	pushl	%ebx
	pushl	%esi
	pushl	%edi
	pushl	-4(%ebp)
	pushl	-8(%ebp)
	pushl	-12(%ebp)
	pushl	-16(%ebp)
	pushl	-24(%ebp)
	pushl	-28(%ebp)
	pushl	-32(%ebp)
	pushl	-36(%ebp)
	pushl	-40(%ebp)
	pushl	-44(%ebp)
	pushl	-48(%ebp)
	pushl	-52(%ebp)
	pushl	$_s16
	call	_printf
	leal	68(%esp),%esp
.L12:
	popl	%edi
	popl	%esi
	popl	%ebx
	leave
	ret


	.align	4
	.global	_main
_main:
	pushl	%ebp
	movl	%esp,%ebp
	pushl	_af
	pushl	_ae
	pushl	_ad
	pushl	_ac
	pushl	_ab
	pushl	_aa
	pushl	_a9
	pushl	_a8
	pushl	_a7
	pushl	_a6
	pushl	_a5
	pushl	_a4
	pushl	_a3
	pushl	_a2
	pushl	_a1
	pushl	_a0
	pushl	$_s16
	call	_printf
	leal	68(%esp),%esp
	pushl	$9
	pushl	$-1
	pushl	$-2147483639
	pushl	$32779
	pushl	$32778
	pushl	$32777
	pushl	$4107
	pushl	$4106
	pushl	$4105
	pushl	$139
	pushl	$138
	pushl	$137
	pushl	$13
	pushl	$12
	pushl	$11
	pushl	$10
	pushl	$_s16
	call	_printf
	leal	68(%esp),%esp
	movl	$10,%eax
	pushl	$9
	pushl	$-1
	pushl	$-2147483639
	pushl	$32779
	pushl	$32778
	pushl	$32777
	pushl	$4107
	pushl	$4106
	pushl	$4105
	pushl	$139
	pushl	$138
	pushl	$137
	pushl	$13
	pushl	$12
	pushl	$11
	pushl	$10
	pushl	$_s16
	call	_printf
	leal	68(%esp),%esp
	pushl	$9
	pushl	$-1
	pushl	$-2147483639
	pushl	$32779
	pushl	$32778
	pushl	$32777
	pushl	$4107
	pushl	$4106
	pushl	$4105
	pushl	$139
	pushl	$138
	pushl	$137
	pushl	$13
	pushl	$12
	pushl	$11
	pushl	$10
	pushl	$_s16
	call	_printf
	leal	68(%esp),%esp
	movl	$10,%eax
	pushl	$9
	pushl	$-1
	pushl	$-2147483639
	pushl	$32779
	pushl	$32778
	pushl	$32777
	pushl	$4107
	pushl	$4106
	pushl	$4105
	pushl	$139
	pushl	$138
	pushl	$137
	pushl	$13
	pushl	$12
	pushl	$11
	pushl	$10
	pushl	$_s16
	call	_printf
	leal	68(%esp),%esp
	pushl	$9
	pushl	$-1
	pushl	$-2147483639
	pushl	$32779
	pushl	$32778
	pushl	$32777
	pushl	$4107
	pushl	$4106
	pushl	$4105
	pushl	$139
	pushl	$138
	pushl	$137
	pushl	$13
	pushl	$12
	pushl	$11
	pushl	$10
	pushl	$_s16
	call	_printf
	leal	68(%esp),%esp
	pushl	$10
	call	_f0
	leal	4(%esp),%esp
	pushl	$10
	call	_f1
	leal	4(%esp),%esp
	pushl	$10
	call	_f2
	leal	4(%esp),%esp
	movl	$0,%eax
	leave
	ret

	.data
	.align	1
	.global	_s16
_s16:
	.byte	37
	.byte	117
	.byte	32
	.byte	37
	.byte	117
	.byte	32
	.byte	37
	.byte	117
	.byte	32
	.byte	37
	.byte	117
	.byte	32
	.byte	37
	.byte	117
	.byte	32
	.byte	37
	.byte	117
	.byte	32
	.byte	37
	.byte	117
	.byte	32
	.byte	37
	.byte	117
	.byte	32
	.byte	37
	.byte	117
	.byte	32
	.byte	37
	.byte	117
	.byte	32
	.byte	37
	.byte	117
	.byte	32
	.byte	37
	.byte	117
	.byte	32
	.byte	37
	.byte	117
	.byte	32
	.byte	37
	.byte	117
	.byte	32
	.byte	37
	.byte	117
	.byte	32
	.byte	37
	.byte	117
	.byte	10
	.byte	0
	.align	4
	.global	_a0
_a0:
	.long	10
	.align	4
	.global	_a1
_a1:
	.long	11
	.align	4
	.global	_a2
_a2:
	.long	12
	.align	4
	.global	_a3
_a3:
	.long	13
	.align	4
	.global	_a4
_a4:
	.long	137
	.align	4
	.global	_a5
_a5:
	.long	138
	.align	4
	.global	_a6
_a6:
	.long	139
	.align	4
	.global	_a7
_a7:
	.long	4105
	.align	4
	.global	_a8
_a8:
	.long	4106
	.align	4
	.global	_a9
_a9:
	.long	4107
	.align	4
	.global	_aa
_aa:
	.long	32777
	.align	4
	.global	_ab
_ab:
	.long	32778
	.align	4
	.global	_ac
_ac:
	.long	32779
	.align	4
	.global	_ad
_ad:
	.long	-2147483639
	.align	4
	.global	_ae
_ae:
	.long	-1
	.align	4
	.global	_af
_af:
	.long	9
