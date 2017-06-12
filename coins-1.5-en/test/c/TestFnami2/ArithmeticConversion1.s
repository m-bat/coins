 .ident "Coins Compiler version: coins-1.4.3.1 + BackEnd-1.0"
/* JavaCG for target:x86 convention:cygwin */

	.text
	.align	4
	.global	_f
_f:
	pushl	%ebp
	movl	%esp,%ebp
	subl	$8,%esp
	pushl	%ebx
	pushl	%esi
	pushl	%edi
	movb	8(%ebp),%al
	movb	%al,-8(%ebp)
	movb	12(%ebp),%al
	movb	%al,-7(%ebp)
	movw	16(%ebp),%ax
	movw	%ax,-6(%ebp)
	movw	20(%ebp),%di
	movl	24(%ebp),%esi
	movl	28(%ebp),%ebx
	movl	32(%ebp),%eax
	movl	%eax,-4(%ebp)
	movzbl	-8(%ebp),%eax
	pushl	%eax
	pushl	$_s1
	call	_printf
	leal	8(%esp),%esp
	movzbw	-7(%ebp),%ax
	movswl	%ax,%eax
	pushl	%eax
	movsbw	-8(%ebp),%ax
	movswl	%ax,%eax
	pushl	%eax
	pushl	$_s2
	call	_printf
	leal	12(%esp),%esp
	movzwl	-6(%ebp),%eax
	pushl	%eax
	movzbw	-7(%ebp),%ax
	movzwl	%ax,%eax
	pushl	%eax
	movsbw	-8(%ebp),%ax
	movzwl	%ax,%eax
	pushl	%eax
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
	movzwl	%di,%eax
	pushl	%eax
	movswl	-6(%ebp),%eax
	pushl	%eax
	movzbl	-7(%ebp),%eax
	pushl	%eax
	movsbl	-8(%ebp),%eax
	pushl	%eax
	pushl	$_s4
	call	_printf
	leal	20(%esp),%esp
	pushl	%esi
	movzwl	%di,%eax
	pushl	%eax
	movswl	-6(%ebp),%eax
	pushl	%eax
	movzbl	-7(%ebp),%eax
	pushl	%eax
	movsbl	-8(%ebp),%eax
	pushl	%eax
	pushl	$_s5
	call	_printf
	leal	24(%esp),%esp
	pushl	%ebx
	pushl	%esi
	movzwl	%di,%eax
	pushl	%eax
	movswl	-6(%ebp),%eax
	pushl	%eax
	movzbl	-7(%ebp),%eax
	pushl	%eax
	movsbl	-8(%ebp),%eax
	pushl	%eax
	pushl	$_s6
	call	_printf
	leal	28(%esp),%esp
	pushl	-4(%ebp)
	pushl	%ebx
	pushl	%esi
	movzwl	%di,%eax
	pushl	%eax
	movswl	-6(%ebp),%eax
	pushl	%eax
	movzbl	-7(%ebp),%eax
	pushl	%eax
	movsbl	-8(%ebp),%eax
	pushl	%eax
	pushl	$_s7
	call	_printf
	leal	32(%esp),%esp
.L3:
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
	movb	$2,%al
	movb	$3,%al
	movw	$4,%ax
	movw	$5,%ax
	movl	$6,%eax
	movl	$7,%eax
	movl	$8,%eax
	movzbl	_ucsc0,%eax
	pushl	%eax
	pushl	$_s1
	call	_printf
	leal	8(%esp),%esp
	movswl	_suc0,%eax
	pushl	%eax
	movswl	_ssc0,%eax
	pushl	%eax
	pushl	$_s2
	call	_printf
	leal	12(%esp),%esp
	movzwl	_uss0,%eax
	pushl	%eax
	movzwl	_usuc0,%eax
	pushl	%eax
	movzwl	_ussc0,%eax
	pushl	%eax
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
	pushl	_ius0
	pushl	_is0
	pushl	_iuc0
	pushl	_isc0
	pushl	$_s4
	call	_printf
	leal	20(%esp),%esp
	pushl	_uii0
	pushl	_uius0
	pushl	_uis0
	pushl	_uiuc0
	pushl	_uisc0
	pushl	$_s5
	call	_printf
	leal	24(%esp),%esp
	pushl	_lui0
	pushl	_li0
	pushl	_lus0
	pushl	_ls0
	pushl	_luc0
	pushl	_lsc0
	pushl	$_s6
	call	_printf
	leal	28(%esp),%esp
	pushl	_ull0
	pushl	_ului0
	pushl	_uli0
	pushl	_ulus0
	pushl	_uls0
	pushl	_uluc0
	pushl	_ulsc0
	pushl	$_s7
	call	_printf
	leal	32(%esp),%esp
	pushl	$2
	pushl	$_s1
	call	_printf
	leal	8(%esp),%esp
	pushl	$3
	pushl	$2
	pushl	$_s2
	call	_printf
	leal	12(%esp),%esp
	pushl	$4
	pushl	$3
	pushl	$2
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
	pushl	$5
	pushl	$4
	pushl	$3
	pushl	$2
	pushl	$_s4
	call	_printf
	leal	20(%esp),%esp
	pushl	$6
	pushl	$5
	pushl	$4
	pushl	$3
	pushl	$2
	pushl	$_s5
	call	_printf
	leal	24(%esp),%esp
	pushl	$7
	pushl	$6
	pushl	$5
	pushl	$4
	pushl	$3
	pushl	$2
	pushl	$_s6
	call	_printf
	leal	28(%esp),%esp
	pushl	$8
	pushl	$7
	pushl	$6
	pushl	$5
	pushl	$4
	pushl	$3
	pushl	$2
	pushl	$_s7
	call	_printf
	leal	32(%esp),%esp
	pushl	$9
	pushl	$8
	pushl	$7
	pushl	$6
	pushl	$5
	pushl	$4
	pushl	$3
	call	_f
	leal	28(%esp),%esp
	movb	$-2,%al
	movb	$-3,%al
	movw	$-4,%ax
	movw	$-5,%ax
	movl	$-6,%eax
	movl	$-7,%eax
	movl	$-8,%eax
	movzbl	_ucsc1,%eax
	pushl	%eax
	pushl	$_s1
	call	_printf
	leal	8(%esp),%esp
	movswl	_suc1,%eax
	pushl	%eax
	movswl	_ssc1,%eax
	pushl	%eax
	pushl	$_s2
	call	_printf
	leal	12(%esp),%esp
	movzwl	_uss1,%eax
	pushl	%eax
	movzwl	_usuc1,%eax
	pushl	%eax
	movzwl	_ussc1,%eax
	pushl	%eax
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
	pushl	_ius1
	pushl	_is1
	pushl	_iuc1
	pushl	_isc1
	pushl	$_s4
	call	_printf
	leal	20(%esp),%esp
	pushl	_uii1
	pushl	_uius1
	pushl	_uis1
	pushl	_uiuc1
	pushl	_uisc1
	pushl	$_s5
	call	_printf
	leal	24(%esp),%esp
	pushl	_lui1
	pushl	_li1
	pushl	_lus1
	pushl	_ls1
	pushl	_luc1
	pushl	_lsc1
	pushl	$_s6
	call	_printf
	leal	28(%esp),%esp
	pushl	_ull1
	pushl	_ului1
	pushl	_uli1
	pushl	_ulus1
	pushl	_uls1
	pushl	_uluc1
	pushl	_ulsc1
	pushl	$_s7
	call	_printf
	leal	32(%esp),%esp
	pushl	$254
	pushl	$_s1
	call	_printf
	leal	8(%esp),%esp
	pushl	$253
	pushl	$-2
	pushl	$_s2
	call	_printf
	leal	12(%esp),%esp
	pushl	$65532
	pushl	$253
	pushl	$65534
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
	pushl	$65531
	pushl	$-4
	pushl	$253
	pushl	$-2
	pushl	$_s4
	call	_printf
	leal	20(%esp),%esp
	pushl	$-6
	pushl	$65531
	pushl	$-4
	pushl	$253
	pushl	$-2
	pushl	$_s5
	call	_printf
	leal	24(%esp),%esp
	pushl	$-7
	pushl	$-6
	pushl	$65531
	pushl	$-4
	pushl	$253
	pushl	$-2
	pushl	$_s6
	call	_printf
	leal	28(%esp),%esp
	pushl	$-8
	pushl	$-7
	pushl	$-6
	pushl	$65531
	pushl	$-4
	pushl	$253
	pushl	$-2
	pushl	$_s7
	call	_printf
	leal	32(%esp),%esp
	pushl	$-9
	pushl	$-8
	pushl	$-7
	pushl	$65530
	pushl	$65531
	pushl	$252
	pushl	$253
	call	_f
	leal	28(%esp),%esp
	movl	$0,%eax
	leave
	ret

	.data
	.align	1
	.global	_s1
_s1:
	.byte	37
	.byte	48
	.byte	56
	.byte	88
	.byte	10
	.byte	0
	.align	1
	.global	_s2
_s2:
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
	.align	1
	.global	_s3
_s3:
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
	.align	1
	.global	_s4
_s4:
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
	.align	1
	.global	_s5
_s5:
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
	.align	1
	.global	_s6
_s6:
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
	.byte	32
	.byte	37
	.byte	48
	.byte	56
	.byte	108
	.byte	88
	.byte	10
	.byte	0
	.align	1
	.global	_s7
_s7:
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
	.align	1
	.global	_ucsc0
_ucsc0:
	.byte	1
	.align	2
	.global	_ssc0
_ssc0:
	.short	1
	.align	2
	.global	_suc0
_suc0:
	.short	2
	.align	2
	.global	_ussc0
_ussc0:
	.short	1
	.align	2
	.global	_usuc0
_usuc0:
	.short	2
	.align	2
	.global	_uss0
_uss0:
	.short	3
	.align	4
	.global	_isc0
_isc0:
	.long	1
	.align	4
	.global	_iuc0
_iuc0:
	.long	2
	.align	4
	.global	_is0
_is0:
	.long	3
	.align	4
	.global	_ius0
_ius0:
	.long	4
	.align	4
	.global	_uisc0
_uisc0:
	.long	1
	.align	4
	.global	_uiuc0
_uiuc0:
	.long	2
	.align	4
	.global	_uis0
_uis0:
	.long	3
	.align	4
	.global	_uius0
_uius0:
	.long	4
	.align	4
	.global	_uii0
_uii0:
	.long	5
	.align	4
	.global	_lsc0
_lsc0:
	.long	1
	.align	4
	.global	_luc0
_luc0:
	.long	2
	.align	4
	.global	_ls0
_ls0:
	.long	3
	.align	4
	.global	_lus0
_lus0:
	.long	4
	.align	4
	.global	_li0
_li0:
	.long	5
	.align	4
	.global	_lui0
_lui0:
	.long	6
	.align	4
	.global	_ulsc0
_ulsc0:
	.long	1
	.align	4
	.global	_uluc0
_uluc0:
	.long	2
	.align	4
	.global	_uls0
_uls0:
	.long	3
	.align	4
	.global	_ulus0
_ulus0:
	.long	4
	.align	4
	.global	_uli0
_uli0:
	.long	5
	.align	4
	.global	_ului0
_ului0:
	.long	6
	.align	4
	.global	_ull0
_ull0:
	.long	7
	.align	1
	.global	_ucsc1
_ucsc1:
	.byte	-1
	.align	2
	.global	_ssc1
_ssc1:
	.short	-1
	.align	2
	.global	_suc1
_suc1:
	.short	254
	.align	2
	.global	_ussc1
_ussc1:
	.short	-1
	.align	2
	.global	_usuc1
_usuc1:
	.short	254
	.align	2
	.global	_uss1
_uss1:
	.short	-3
	.align	4
	.global	_isc1
_isc1:
	.long	-1
	.align	4
	.global	_iuc1
_iuc1:
	.long	254
	.align	4
	.global	_is1
_is1:
	.long	-3
	.align	4
	.global	_ius1
_ius1:
	.long	65532
	.align	4
	.global	_uisc1
_uisc1:
	.long	-1
	.align	4
	.global	_uiuc1
_uiuc1:
	.long	254
	.align	4
	.global	_uis1
_uis1:
	.long	-3
	.align	4
	.global	_uius1
_uius1:
	.long	65532
	.align	4
	.global	_uii1
_uii1:
	.long	-5
	.align	4
	.global	_lsc1
_lsc1:
	.long	-1
	.align	4
	.global	_luc1
_luc1:
	.long	254
	.align	4
	.global	_ls1
_ls1:
	.long	-3
	.align	4
	.global	_lus1
_lus1:
	.long	65532
	.align	4
	.global	_li1
_li1:
	.long	-5
	.align	4
	.global	_lui1
_lui1:
	.long	-6
	.align	4
	.global	_ulsc1
_ulsc1:
	.long	-1
	.align	4
	.global	_uluc1
_uluc1:
	.long	254
	.align	4
	.global	_uls1
_uls1:
	.long	-3
	.align	4
	.global	_ulus1
_ulus1:
	.long	65532
	.align	4
	.global	_uli1
_uli1:
	.long	-5
	.align	4
	.global	_ului1
_ului1:
	.long	-6
	.align	4
	.global	_ull1
_ull1:
	.long	-7
