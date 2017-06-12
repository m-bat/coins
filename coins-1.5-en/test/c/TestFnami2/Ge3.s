 .ident "Coins Compiler version: coins-1.4.3.1 + BackEnd-1.0"
/* JavaCG for target:x86 convention:cygwin */

	.text
	.align	4
	.global	_f0
_f0:
	pushl	%ebp
	movl	%esp,%ebp
	subl	$12,%esp
	pushl	%ebx
	pushl	%esi
	pushl	%edi
	flds	8(%ebp)
	fstps	-4(%ebp)
	flds	.LC1
	fchs
	flds	-4(%ebp)
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L4
.L3:
	movl	$1,-12(%ebp)
	jmp	.L5
.L4:
	movl	$0,-12(%ebp)
.L5:
	flds	.LC2
	fchs
	flds	-4(%ebp)
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L7
.L6:
	movl	$1,-8(%ebp)
	jmp	.L8
.L7:
	movl	$0,-8(%ebp)
.L8:
	flds	.LC3
	fchs
	flds	-4(%ebp)
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L10
.L9:
	movl	$1,%edi
	jmp	.L11
.L10:
	movl	$0,%edi
.L11:
	fldz
	fchs
	flds	-4(%ebp)
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L13
.L12:
	movl	$1,%esi
	jmp	.L14
.L13:
	movl	$0,%esi
.L14:
	flds	-4(%ebp)
	fldz
	fxch
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L16
.L15:
	movl	$1,%ebx
	jmp	.L17
.L16:
	movl	$0,%ebx
.L17:
	flds	-4(%ebp)
	fcomps	.LC3
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L19
.L18:
	movl	$1,%edx
	jmp	.L20
.L19:
	movl	$0,%edx
.L20:
	flds	-4(%ebp)
	fcomps	.LC2
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L22
.L21:
	movl	$1,%ecx
	jmp	.L23
.L22:
	movl	$0,%ecx
.L23:
	flds	-4(%ebp)
	fcomps	.LC1
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L25
.L24:
	movl	$1,%eax
	jmp	.L26
.L25:
	movl	$0,%eax
.L26:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	%ebx
	pushl	%esi
	pushl	%edi
	pushl	-8(%ebp)
	pushl	-12(%ebp)
	pushl	$_s8
	call	_printf
	leal	36(%esp),%esp
.L27:
	popl	%edi
	popl	%esi
	popl	%ebx
	leave
	ret


	.align	4
	.global	_f1
_f1:
	pushl	%ebp
	movl	%esp,%ebp
	subl	$12,%esp
	pushl	%ebx
	pushl	%esi
	pushl	%edi
	flds	8(%ebp)
	fstps	-4(%ebp)
	flds	.LC1
	fchs
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L31
.L30:
	movl	$1,-12(%ebp)
	jmp	.L32
.L31:
	movl	$0,-12(%ebp)
.L32:
	flds	.LC2
	fchs
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L34
.L33:
	movl	$1,-8(%ebp)
	jmp	.L35
.L34:
	movl	$0,-8(%ebp)
.L35:
	flds	.LC3
	fchs
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L37
.L36:
	movl	$1,%edi
	jmp	.L38
.L37:
	movl	$0,%edi
.L38:
	fldz
	fchs
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L40
.L39:
	movl	$1,%esi
	jmp	.L41
.L40:
	movl	$0,%esi
.L41:
	fldz
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L43
.L42:
	movl	$1,%ebx
	jmp	.L44
.L43:
	movl	$0,%ebx
.L44:
	flds	.LC3
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L46
.L45:
	movl	$1,%edx
	jmp	.L47
.L46:
	movl	$0,%edx
.L47:
	flds	.LC2
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L49
.L48:
	movl	$1,%ecx
	jmp	.L50
.L49:
	movl	$0,%ecx
.L50:
	flds	.LC1
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L52
.L51:
	movl	$1,%eax
	jmp	.L53
.L52:
	movl	$0,%eax
.L53:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	%ebx
	pushl	%esi
	pushl	%edi
	pushl	-8(%ebp)
	pushl	-12(%ebp)
	pushl	$_s8
	call	_printf
	leal	36(%esp),%esp
.L54:
	popl	%edi
	popl	%esi
	popl	%ebx
	leave
	ret


	.align	4
	.global	_g0
_g0:
	pushl	%ebp
	movl	%esp,%ebp
	subl	$4,%esp
	flds	8(%ebp)
	fstps	-4(%ebp)
	flds	-4(%ebp)
	fcomps	.LC4
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L58
.L57:
	movl	$1,%edx
	jmp	.L59
.L58:
	movl	$0,%edx
.L59:
	flds	-4(%ebp)
	fcomps	.LC5
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L61
.L60:
	movl	$1,%ecx
	jmp	.L62
.L61:
	movl	$0,%ecx
.L62:
	flds	-4(%ebp)
	fcomps	.LC5
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L64
.L63:
	movl	$1,%eax
	jmp	.L65
.L64:
	movl	$0,%eax
.L65:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
.L66:
	leave
	ret


	.align	4
	.global	_g1
_g1:
	pushl	%ebp
	movl	%esp,%ebp
	subl	$4,%esp
	flds	8(%ebp)
	fstps	-4(%ebp)
	flds	.LC4
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L70
.L69:
	movl	$1,%edx
	jmp	.L71
.L70:
	movl	$0,%edx
.L71:
	flds	.LC5
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L73
.L72:
	movl	$1,%ecx
	jmp	.L74
.L73:
	movl	$0,%ecx
.L74:
	flds	.LC5
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L76
.L75:
	movl	$1,%eax
	jmp	.L77
.L76:
	movl	$0,%eax
.L77:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
.L78:
	leave
	ret


	.align	4
	.global	_op
_op:
	pushl	%ebp
	movl	%esp,%ebp
	subl	$8,%esp
	flds	8(%ebp)
	fstps	-4(%ebp)
	flds	12(%ebp)
	fstps	-8(%ebp)
	flds	-4(%ebp)
	fcomps	-8(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L82
.L81:
	movl	$1,%eax
	jmp	.L83
.L82:
	movl	$0,%eax
.L83:
	leave
	ret


	.align	4
	.global	_f2
_f2:
	pushl	%ebp
	movl	%esp,%ebp
	subl	$20,%esp
	pushl	%ebx
	pushl	%esi
	pushl	%edi
	flds	8(%ebp)
	fstps	-4(%ebp)
	flds	.LC1
	fchs
	sub	$4,%esp
	fstps	(%esp)
	pushl	-4(%ebp)
	call	_op
	movl	%eax,-20(%ebp)
	leal	8(%esp),%esp
	flds	.LC2
	fchs
	sub	$4,%esp
	fstps	(%esp)
	pushl	-4(%ebp)
	call	_op
	movl	%eax,-16(%ebp)
	leal	8(%esp),%esp
	flds	.LC3
	fchs
	sub	$4,%esp
	fstps	(%esp)
	pushl	-4(%ebp)
	call	_op
	movl	%eax,-12(%ebp)
	leal	8(%esp),%esp
	fldz
	fchs
	sub	$4,%esp
	fstps	(%esp)
	pushl	-4(%ebp)
	call	_op
	movl	%eax,-8(%ebp)
	leal	8(%esp),%esp
	fldz
	sub	$4,%esp
	fstps	(%esp)
	pushl	-4(%ebp)
	call	_op
	movl	%eax,%edi
	leal	8(%esp),%esp
	pushl	.LC3
	pushl	-4(%ebp)
	call	_op
	movl	%eax,%esi
	leal	8(%esp),%esp
	pushl	.LC2
	pushl	-4(%ebp)
	call	_op
	movl	%eax,%ebx
	leal	8(%esp),%esp
	pushl	.LC1
	pushl	-4(%ebp)
	call	_op
	leal	8(%esp),%esp
	pushl	%eax
	pushl	%ebx
	pushl	%esi
	pushl	%edi
	pushl	-8(%ebp)
	pushl	-12(%ebp)
	pushl	-16(%ebp)
	pushl	-20(%ebp)
	pushl	$_s8
	call	_printf
	leal	36(%esp),%esp
.L87:
	popl	%edi
	popl	%esi
	popl	%ebx
	leave
	ret


	.align	4
	.global	_g2
_g2:
	pushl	%ebp
	movl	%esp,%ebp
	subl	$4,%esp
	pushl	%ebx
	pushl	%esi
	flds	8(%ebp)
	fstps	-4(%ebp)
	pushl	.LC4
	pushl	-4(%ebp)
	call	_op
	movl	%eax,%esi
	leal	8(%esp),%esp
	pushl	.LC5
	pushl	-4(%ebp)
	call	_op
	movl	%eax,%ebx
	leal	8(%esp),%esp
	pushl	.LC5
	pushl	-4(%ebp)
	call	_op
	leal	8(%esp),%esp
	pushl	%eax
	pushl	%ebx
	pushl	%esi
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
.L90:
	popl	%esi
	popl	%ebx
	leave
	ret


	.align	4
	.global	_main1
_main1:
	pushl	%ebp
	movl	%esp,%ebp
	subl	$132,%esp
	pushl	%ebx
	pushl	%esi
	pushl	%edi
	flds	.LC1
	fchs
	fstps	-4(%ebp)
	flds	.LC1
	fchs
	flds	-4(%ebp)
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L94
.L93:
	movl	$1,-132(%ebp)
	jmp	.L95
.L94:
	movl	$0,-132(%ebp)
.L95:
	flds	.LC2
	fchs
	flds	-4(%ebp)
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L97
.L96:
	movl	$1,-68(%ebp)
	jmp	.L98
.L97:
	movl	$0,-68(%ebp)
.L98:
	flds	.LC3
	fchs
	flds	-4(%ebp)
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L100
.L99:
	movl	$1,%edi
	jmp	.L101
.L100:
	movl	$0,%edi
.L101:
	fldz
	fchs
	flds	-4(%ebp)
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L103
.L102:
	movl	$1,%esi
	jmp	.L104
.L103:
	movl	$0,%esi
.L104:
	flds	-4(%ebp)
	fldz
	fxch
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L106
.L105:
	movl	$1,%ebx
	jmp	.L107
.L106:
	movl	$0,%ebx
.L107:
	flds	-4(%ebp)
	fcomps	.LC3
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L109
.L108:
	movl	$1,%edx
	jmp	.L110
.L109:
	movl	$0,%edx
.L110:
	flds	-4(%ebp)
	fcomps	.LC2
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L112
.L111:
	movl	$1,%ecx
	jmp	.L113
.L112:
	movl	$0,%ecx
.L113:
	flds	-4(%ebp)
	fcomps	.LC1
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L115
.L114:
	movl	$1,%eax
	jmp	.L116
.L115:
	movl	$0,%eax
.L116:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	%ebx
	pushl	%esi
	pushl	%edi
	pushl	-68(%ebp)
	pushl	-132(%ebp)
	pushl	$_s8
	call	_printf
	leal	36(%esp),%esp
	flds	.LC1
	fchs
	flds	-4(%ebp)
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L118
.L117:
	movl	$1,-128(%ebp)
	jmp	.L119
.L118:
	movl	$0,-128(%ebp)
.L119:
	flds	.LC2
	fchs
	flds	-4(%ebp)
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L121
.L120:
	movl	$1,-64(%ebp)
	jmp	.L122
.L121:
	movl	$0,-64(%ebp)
.L122:
	flds	.LC3
	fchs
	flds	-4(%ebp)
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L124
.L123:
	movl	$1,%edi
	jmp	.L125
.L124:
	movl	$0,%edi
.L125:
	fldz
	fchs
	flds	-4(%ebp)
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L127
.L126:
	movl	$1,%esi
	jmp	.L128
.L127:
	movl	$0,%esi
.L128:
	flds	-4(%ebp)
	fldz
	fxch
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L130
.L129:
	movl	$1,%ebx
	jmp	.L131
.L130:
	movl	$0,%ebx
.L131:
	flds	-4(%ebp)
	fcomps	.LC3
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L133
.L132:
	movl	$1,%edx
	jmp	.L134
.L133:
	movl	$0,%edx
.L134:
	flds	-4(%ebp)
	fcomps	.LC2
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L136
.L135:
	movl	$1,%ecx
	jmp	.L137
.L136:
	movl	$0,%ecx
.L137:
	flds	-4(%ebp)
	fcomps	.LC1
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L139
.L138:
	movl	$1,%eax
	jmp	.L140
.L139:
	movl	$0,%eax
.L140:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	%ebx
	pushl	%esi
	pushl	%edi
	pushl	-64(%ebp)
	pushl	-128(%ebp)
	pushl	$_s8
	call	_printf
	leal	36(%esp),%esp
	flds	.LC2
	fchs
	fstps	-4(%ebp)
	flds	.LC1
	fchs
	flds	-4(%ebp)
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L142
.L141:
	movl	$1,-124(%ebp)
	jmp	.L143
.L142:
	movl	$0,-124(%ebp)
.L143:
	flds	.LC2
	fchs
	flds	-4(%ebp)
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L145
.L144:
	movl	$1,-60(%ebp)
	jmp	.L146
.L145:
	movl	$0,-60(%ebp)
.L146:
	flds	.LC3
	fchs
	flds	-4(%ebp)
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L148
.L147:
	movl	$1,%edi
	jmp	.L149
.L148:
	movl	$0,%edi
.L149:
	fldz
	fchs
	flds	-4(%ebp)
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L151
.L150:
	movl	$1,%esi
	jmp	.L152
.L151:
	movl	$0,%esi
.L152:
	flds	-4(%ebp)
	fldz
	fxch
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L154
.L153:
	movl	$1,%ebx
	jmp	.L155
.L154:
	movl	$0,%ebx
.L155:
	flds	-4(%ebp)
	fcomps	.LC3
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L157
.L156:
	movl	$1,%edx
	jmp	.L158
.L157:
	movl	$0,%edx
.L158:
	flds	-4(%ebp)
	fcomps	.LC2
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L160
.L159:
	movl	$1,%ecx
	jmp	.L161
.L160:
	movl	$0,%ecx
.L161:
	flds	-4(%ebp)
	fcomps	.LC1
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L163
.L162:
	movl	$1,%eax
	jmp	.L164
.L163:
	movl	$0,%eax
.L164:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	%ebx
	pushl	%esi
	pushl	%edi
	pushl	-60(%ebp)
	pushl	-124(%ebp)
	pushl	$_s8
	call	_printf
	leal	36(%esp),%esp
	flds	.LC1
	fchs
	flds	-4(%ebp)
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L166
.L165:
	movl	$1,-120(%ebp)
	jmp	.L167
.L166:
	movl	$0,-120(%ebp)
.L167:
	flds	.LC2
	fchs
	flds	-4(%ebp)
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L169
.L168:
	movl	$1,-56(%ebp)
	jmp	.L170
.L169:
	movl	$0,-56(%ebp)
.L170:
	flds	.LC3
	fchs
	flds	-4(%ebp)
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L172
.L171:
	movl	$1,%edi
	jmp	.L173
.L172:
	movl	$0,%edi
.L173:
	fldz
	fchs
	flds	-4(%ebp)
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L175
.L174:
	movl	$1,%esi
	jmp	.L176
.L175:
	movl	$0,%esi
.L176:
	flds	-4(%ebp)
	fldz
	fxch
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L178
.L177:
	movl	$1,%ebx
	jmp	.L179
.L178:
	movl	$0,%ebx
.L179:
	flds	-4(%ebp)
	fcomps	.LC3
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L181
.L180:
	movl	$1,%edx
	jmp	.L182
.L181:
	movl	$0,%edx
.L182:
	flds	-4(%ebp)
	fcomps	.LC2
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L184
.L183:
	movl	$1,%ecx
	jmp	.L185
.L184:
	movl	$0,%ecx
.L185:
	flds	-4(%ebp)
	fcomps	.LC1
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L187
.L186:
	movl	$1,%eax
	jmp	.L188
.L187:
	movl	$0,%eax
.L188:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	%ebx
	pushl	%esi
	pushl	%edi
	pushl	-56(%ebp)
	pushl	-120(%ebp)
	pushl	$_s8
	call	_printf
	leal	36(%esp),%esp
	flds	.LC3
	fchs
	fstps	-4(%ebp)
	flds	.LC1
	fchs
	flds	-4(%ebp)
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L190
.L189:
	movl	$1,-116(%ebp)
	jmp	.L191
.L190:
	movl	$0,-116(%ebp)
.L191:
	flds	.LC2
	fchs
	flds	-4(%ebp)
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L193
.L192:
	movl	$1,-52(%ebp)
	jmp	.L194
.L193:
	movl	$0,-52(%ebp)
.L194:
	flds	.LC3
	fchs
	flds	-4(%ebp)
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L196
.L195:
	movl	$1,%edi
	jmp	.L197
.L196:
	movl	$0,%edi
.L197:
	fldz
	fchs
	flds	-4(%ebp)
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L199
.L198:
	movl	$1,%esi
	jmp	.L200
.L199:
	movl	$0,%esi
.L200:
	flds	-4(%ebp)
	fldz
	fxch
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L202
.L201:
	movl	$1,%ebx
	jmp	.L203
.L202:
	movl	$0,%ebx
.L203:
	flds	-4(%ebp)
	fcomps	.LC3
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L205
.L204:
	movl	$1,%edx
	jmp	.L206
.L205:
	movl	$0,%edx
.L206:
	flds	-4(%ebp)
	fcomps	.LC2
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L208
.L207:
	movl	$1,%ecx
	jmp	.L209
.L208:
	movl	$0,%ecx
.L209:
	flds	-4(%ebp)
	fcomps	.LC1
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L211
.L210:
	movl	$1,%eax
	jmp	.L212
.L211:
	movl	$0,%eax
.L212:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	%ebx
	pushl	%esi
	pushl	%edi
	pushl	-52(%ebp)
	pushl	-116(%ebp)
	pushl	$_s8
	call	_printf
	leal	36(%esp),%esp
	flds	.LC1
	fchs
	flds	-4(%ebp)
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L214
.L213:
	movl	$1,-112(%ebp)
	jmp	.L215
.L214:
	movl	$0,-112(%ebp)
.L215:
	flds	.LC2
	fchs
	flds	-4(%ebp)
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L217
.L216:
	movl	$1,-48(%ebp)
	jmp	.L218
.L217:
	movl	$0,-48(%ebp)
.L218:
	flds	.LC3
	fchs
	flds	-4(%ebp)
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L220
.L219:
	movl	$1,%edi
	jmp	.L221
.L220:
	movl	$0,%edi
.L221:
	fldz
	fchs
	flds	-4(%ebp)
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L223
.L222:
	movl	$1,%esi
	jmp	.L224
.L223:
	movl	$0,%esi
.L224:
	flds	-4(%ebp)
	fldz
	fxch
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L226
.L225:
	movl	$1,%ebx
	jmp	.L227
.L226:
	movl	$0,%ebx
.L227:
	flds	-4(%ebp)
	fcomps	.LC3
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L229
.L228:
	movl	$1,%edx
	jmp	.L230
.L229:
	movl	$0,%edx
.L230:
	flds	-4(%ebp)
	fcomps	.LC2
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L232
.L231:
	movl	$1,%ecx
	jmp	.L233
.L232:
	movl	$0,%ecx
.L233:
	flds	-4(%ebp)
	fcomps	.LC1
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L235
.L234:
	movl	$1,%eax
	jmp	.L236
.L235:
	movl	$0,%eax
.L236:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	%ebx
	pushl	%esi
	pushl	%edi
	pushl	-48(%ebp)
	pushl	-112(%ebp)
	pushl	$_s8
	call	_printf
	leal	36(%esp),%esp
	fldz
	fchs
	fstps	-4(%ebp)
	flds	.LC1
	fchs
	flds	-4(%ebp)
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L238
.L237:
	movl	$1,-108(%ebp)
	jmp	.L239
.L238:
	movl	$0,-108(%ebp)
.L239:
	flds	.LC2
	fchs
	flds	-4(%ebp)
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L241
.L240:
	movl	$1,-44(%ebp)
	jmp	.L242
.L241:
	movl	$0,-44(%ebp)
.L242:
	flds	.LC3
	fchs
	flds	-4(%ebp)
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L244
.L243:
	movl	$1,%edi
	jmp	.L245
.L244:
	movl	$0,%edi
.L245:
	fldz
	fchs
	flds	-4(%ebp)
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L247
.L246:
	movl	$1,%esi
	jmp	.L248
.L247:
	movl	$0,%esi
.L248:
	flds	-4(%ebp)
	fldz
	fxch
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L250
.L249:
	movl	$1,%ebx
	jmp	.L251
.L250:
	movl	$0,%ebx
.L251:
	flds	-4(%ebp)
	fcomps	.LC3
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L253
.L252:
	movl	$1,%edx
	jmp	.L254
.L253:
	movl	$0,%edx
.L254:
	flds	-4(%ebp)
	fcomps	.LC2
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L256
.L255:
	movl	$1,%ecx
	jmp	.L257
.L256:
	movl	$0,%ecx
.L257:
	flds	-4(%ebp)
	fcomps	.LC1
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L259
.L258:
	movl	$1,%eax
	jmp	.L260
.L259:
	movl	$0,%eax
.L260:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	%ebx
	pushl	%esi
	pushl	%edi
	pushl	-44(%ebp)
	pushl	-108(%ebp)
	pushl	$_s8
	call	_printf
	leal	36(%esp),%esp
	flds	.LC1
	fchs
	flds	-4(%ebp)
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L262
.L261:
	movl	$1,-104(%ebp)
	jmp	.L263
.L262:
	movl	$0,-104(%ebp)
.L263:
	flds	.LC2
	fchs
	flds	-4(%ebp)
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L265
.L264:
	movl	$1,-40(%ebp)
	jmp	.L266
.L265:
	movl	$0,-40(%ebp)
.L266:
	flds	.LC3
	fchs
	flds	-4(%ebp)
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L268
.L267:
	movl	$1,%edi
	jmp	.L269
.L268:
	movl	$0,%edi
.L269:
	fldz
	fchs
	flds	-4(%ebp)
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L271
.L270:
	movl	$1,%esi
	jmp	.L272
.L271:
	movl	$0,%esi
.L272:
	flds	-4(%ebp)
	fldz
	fxch
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L274
.L273:
	movl	$1,%ebx
	jmp	.L275
.L274:
	movl	$0,%ebx
.L275:
	flds	-4(%ebp)
	fcomps	.LC3
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L277
.L276:
	movl	$1,%edx
	jmp	.L278
.L277:
	movl	$0,%edx
.L278:
	flds	-4(%ebp)
	fcomps	.LC2
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L280
.L279:
	movl	$1,%ecx
	jmp	.L281
.L280:
	movl	$0,%ecx
.L281:
	flds	-4(%ebp)
	fcomps	.LC1
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L283
.L282:
	movl	$1,%eax
	jmp	.L284
.L283:
	movl	$0,%eax
.L284:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	%ebx
	pushl	%esi
	pushl	%edi
	pushl	-40(%ebp)
	pushl	-104(%ebp)
	pushl	$_s8
	call	_printf
	leal	36(%esp),%esp
	fldz
	fstps	-4(%ebp)
	flds	.LC1
	fchs
	fldz
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L286
.L285:
	movl	$1,-100(%ebp)
	jmp	.L287
.L286:
	movl	$0,-100(%ebp)
.L287:
	flds	.LC2
	fchs
	flds	-4(%ebp)
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L289
.L288:
	movl	$1,-36(%ebp)
	jmp	.L290
.L289:
	movl	$0,-36(%ebp)
.L290:
	flds	.LC3
	fchs
	flds	-4(%ebp)
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L292
.L291:
	movl	$1,%edi
	jmp	.L293
.L292:
	movl	$0,%edi
.L293:
	fldz
	fchs
	flds	-4(%ebp)
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L295
.L294:
	movl	$1,%esi
	jmp	.L296
.L295:
	movl	$0,%esi
.L296:
	flds	-4(%ebp)
	fldz
	fxch
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L298
.L297:
	movl	$1,%ebx
	jmp	.L299
.L298:
	movl	$0,%ebx
.L299:
	flds	-4(%ebp)
	fcomps	.LC3
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L301
.L300:
	movl	$1,%edx
	jmp	.L302
.L301:
	movl	$0,%edx
.L302:
	flds	-4(%ebp)
	fcomps	.LC2
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L304
.L303:
	movl	$1,%ecx
	jmp	.L305
.L304:
	movl	$0,%ecx
.L305:
	flds	-4(%ebp)
	fcomps	.LC1
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L307
.L306:
	movl	$1,%eax
	jmp	.L308
.L307:
	movl	$0,%eax
.L308:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	%ebx
	pushl	%esi
	pushl	%edi
	pushl	-36(%ebp)
	pushl	-100(%ebp)
	pushl	$_s8
	call	_printf
	leal	36(%esp),%esp
	flds	.LC1
	fchs
	flds	-4(%ebp)
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L310
.L309:
	movl	$1,-96(%ebp)
	jmp	.L311
.L310:
	movl	$0,-96(%ebp)
.L311:
	flds	.LC2
	fchs
	flds	-4(%ebp)
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L313
.L312:
	movl	$1,-32(%ebp)
	jmp	.L314
.L313:
	movl	$0,-32(%ebp)
.L314:
	flds	.LC3
	fchs
	flds	-4(%ebp)
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L316
.L315:
	movl	$1,%edi
	jmp	.L317
.L316:
	movl	$0,%edi
.L317:
	fldz
	fchs
	flds	-4(%ebp)
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L319
.L318:
	movl	$1,%esi
	jmp	.L320
.L319:
	movl	$0,%esi
.L320:
	flds	-4(%ebp)
	fldz
	fxch
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L322
.L321:
	movl	$1,%ebx
	jmp	.L323
.L322:
	movl	$0,%ebx
.L323:
	flds	-4(%ebp)
	fcomps	.LC3
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L325
.L324:
	movl	$1,%edx
	jmp	.L326
.L325:
	movl	$0,%edx
.L326:
	flds	-4(%ebp)
	fcomps	.LC2
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L328
.L327:
	movl	$1,%ecx
	jmp	.L329
.L328:
	movl	$0,%ecx
.L329:
	flds	-4(%ebp)
	fcomps	.LC1
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L331
.L330:
	movl	$1,%eax
	jmp	.L332
.L331:
	movl	$0,%eax
.L332:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	%ebx
	pushl	%esi
	pushl	%edi
	pushl	-32(%ebp)
	pushl	-96(%ebp)
	pushl	$_s8
	call	_printf
	leal	36(%esp),%esp
	flds	.LC3
	fstps	-4(%ebp)
	flds	.LC1
	fchs
	flds	.LC3
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L334
.L333:
	movl	$1,-92(%ebp)
	jmp	.L335
.L334:
	movl	$0,-92(%ebp)
.L335:
	flds	.LC2
	fchs
	flds	-4(%ebp)
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L337
.L336:
	movl	$1,-28(%ebp)
	jmp	.L338
.L337:
	movl	$0,-28(%ebp)
.L338:
	flds	.LC3
	fchs
	flds	-4(%ebp)
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L340
.L339:
	movl	$1,%edi
	jmp	.L341
.L340:
	movl	$0,%edi
.L341:
	fldz
	fchs
	flds	-4(%ebp)
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L343
.L342:
	movl	$1,%esi
	jmp	.L344
.L343:
	movl	$0,%esi
.L344:
	flds	-4(%ebp)
	fldz
	fxch
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L346
.L345:
	movl	$1,%ebx
	jmp	.L347
.L346:
	movl	$0,%ebx
.L347:
	flds	-4(%ebp)
	fcomps	.LC3
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L349
.L348:
	movl	$1,%edx
	jmp	.L350
.L349:
	movl	$0,%edx
.L350:
	flds	-4(%ebp)
	fcomps	.LC2
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L352
.L351:
	movl	$1,%ecx
	jmp	.L353
.L352:
	movl	$0,%ecx
.L353:
	flds	-4(%ebp)
	fcomps	.LC1
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L355
.L354:
	movl	$1,%eax
	jmp	.L356
.L355:
	movl	$0,%eax
.L356:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	%ebx
	pushl	%esi
	pushl	%edi
	pushl	-28(%ebp)
	pushl	-92(%ebp)
	pushl	$_s8
	call	_printf
	leal	36(%esp),%esp
	flds	.LC1
	fchs
	flds	-4(%ebp)
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L358
.L357:
	movl	$1,-88(%ebp)
	jmp	.L359
.L358:
	movl	$0,-88(%ebp)
.L359:
	flds	.LC2
	fchs
	flds	-4(%ebp)
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L361
.L360:
	movl	$1,-24(%ebp)
	jmp	.L362
.L361:
	movl	$0,-24(%ebp)
.L362:
	flds	.LC3
	fchs
	flds	-4(%ebp)
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L364
.L363:
	movl	$1,%edi
	jmp	.L365
.L364:
	movl	$0,%edi
.L365:
	fldz
	fchs
	flds	-4(%ebp)
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L367
.L366:
	movl	$1,%esi
	jmp	.L368
.L367:
	movl	$0,%esi
.L368:
	flds	-4(%ebp)
	fldz
	fxch
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L370
.L369:
	movl	$1,%ebx
	jmp	.L371
.L370:
	movl	$0,%ebx
.L371:
	flds	-4(%ebp)
	fcomps	.LC3
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L373
.L372:
	movl	$1,%edx
	jmp	.L374
.L373:
	movl	$0,%edx
.L374:
	flds	-4(%ebp)
	fcomps	.LC2
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L376
.L375:
	movl	$1,%ecx
	jmp	.L377
.L376:
	movl	$0,%ecx
.L377:
	flds	-4(%ebp)
	fcomps	.LC1
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L379
.L378:
	movl	$1,%eax
	jmp	.L380
.L379:
	movl	$0,%eax
.L380:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	%ebx
	pushl	%esi
	pushl	%edi
	pushl	-24(%ebp)
	pushl	-88(%ebp)
	pushl	$_s8
	call	_printf
	leal	36(%esp),%esp
	flds	.LC2
	fstps	-4(%ebp)
	flds	.LC1
	fchs
	flds	.LC2
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L382
.L381:
	movl	$1,-84(%ebp)
	jmp	.L383
.L382:
	movl	$0,-84(%ebp)
.L383:
	flds	.LC2
	fchs
	flds	-4(%ebp)
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L385
.L384:
	movl	$1,-20(%ebp)
	jmp	.L386
.L385:
	movl	$0,-20(%ebp)
.L386:
	flds	.LC3
	fchs
	flds	-4(%ebp)
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L388
.L387:
	movl	$1,%edi
	jmp	.L389
.L388:
	movl	$0,%edi
.L389:
	fldz
	fchs
	flds	-4(%ebp)
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L391
.L390:
	movl	$1,%esi
	jmp	.L392
.L391:
	movl	$0,%esi
.L392:
	flds	-4(%ebp)
	fldz
	fxch
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L394
.L393:
	movl	$1,%ebx
	jmp	.L395
.L394:
	movl	$0,%ebx
.L395:
	flds	-4(%ebp)
	fcomps	.LC3
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L397
.L396:
	movl	$1,%edx
	jmp	.L398
.L397:
	movl	$0,%edx
.L398:
	flds	-4(%ebp)
	fcomps	.LC2
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L400
.L399:
	movl	$1,%ecx
	jmp	.L401
.L400:
	movl	$0,%ecx
.L401:
	flds	-4(%ebp)
	fcomps	.LC1
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L403
.L402:
	movl	$1,%eax
	jmp	.L404
.L403:
	movl	$0,%eax
.L404:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	%ebx
	pushl	%esi
	pushl	%edi
	pushl	-20(%ebp)
	pushl	-84(%ebp)
	pushl	$_s8
	call	_printf
	leal	36(%esp),%esp
	flds	.LC1
	fchs
	flds	-4(%ebp)
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L406
.L405:
	movl	$1,-80(%ebp)
	jmp	.L407
.L406:
	movl	$0,-80(%ebp)
.L407:
	flds	.LC2
	fchs
	flds	-4(%ebp)
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L409
.L408:
	movl	$1,-16(%ebp)
	jmp	.L410
.L409:
	movl	$0,-16(%ebp)
.L410:
	flds	.LC3
	fchs
	flds	-4(%ebp)
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L412
.L411:
	movl	$1,%edi
	jmp	.L413
.L412:
	movl	$0,%edi
.L413:
	fldz
	fchs
	flds	-4(%ebp)
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L415
.L414:
	movl	$1,%esi
	jmp	.L416
.L415:
	movl	$0,%esi
.L416:
	flds	-4(%ebp)
	fldz
	fxch
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L418
.L417:
	movl	$1,%ebx
	jmp	.L419
.L418:
	movl	$0,%ebx
.L419:
	flds	-4(%ebp)
	fcomps	.LC3
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L421
.L420:
	movl	$1,%edx
	jmp	.L422
.L421:
	movl	$0,%edx
.L422:
	flds	-4(%ebp)
	fcomps	.LC2
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L424
.L423:
	movl	$1,%ecx
	jmp	.L425
.L424:
	movl	$0,%ecx
.L425:
	flds	-4(%ebp)
	fcomps	.LC1
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L427
.L426:
	movl	$1,%eax
	jmp	.L428
.L427:
	movl	$0,%eax
.L428:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	%ebx
	pushl	%esi
	pushl	%edi
	pushl	-16(%ebp)
	pushl	-80(%ebp)
	pushl	$_s8
	call	_printf
	leal	36(%esp),%esp
	flds	.LC1
	fstps	-4(%ebp)
	flds	.LC1
	fchs
	flds	.LC1
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L430
.L429:
	movl	$1,-76(%ebp)
	jmp	.L431
.L430:
	movl	$0,-76(%ebp)
.L431:
	flds	.LC2
	fchs
	flds	-4(%ebp)
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L433
.L432:
	movl	$1,-12(%ebp)
	jmp	.L434
.L433:
	movl	$0,-12(%ebp)
.L434:
	flds	.LC3
	fchs
	flds	-4(%ebp)
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L436
.L435:
	movl	$1,%edi
	jmp	.L437
.L436:
	movl	$0,%edi
.L437:
	fldz
	fchs
	flds	-4(%ebp)
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L439
.L438:
	movl	$1,%esi
	jmp	.L440
.L439:
	movl	$0,%esi
.L440:
	flds	-4(%ebp)
	fldz
	fxch
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L442
.L441:
	movl	$1,%ebx
	jmp	.L443
.L442:
	movl	$0,%ebx
.L443:
	flds	-4(%ebp)
	fcomps	.LC3
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L445
.L444:
	movl	$1,%edx
	jmp	.L446
.L445:
	movl	$0,%edx
.L446:
	flds	-4(%ebp)
	fcomps	.LC2
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L448
.L447:
	movl	$1,%ecx
	jmp	.L449
.L448:
	movl	$0,%ecx
.L449:
	flds	-4(%ebp)
	fcomps	.LC1
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L451
.L450:
	movl	$1,%eax
	jmp	.L452
.L451:
	movl	$0,%eax
.L452:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	%ebx
	pushl	%esi
	pushl	%edi
	pushl	-12(%ebp)
	pushl	-76(%ebp)
	pushl	$_s8
	call	_printf
	leal	36(%esp),%esp
	flds	.LC1
	fchs
	flds	-4(%ebp)
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L454
.L453:
	movl	$1,-72(%ebp)
	jmp	.L455
.L454:
	movl	$0,-72(%ebp)
.L455:
	flds	.LC2
	fchs
	flds	-4(%ebp)
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L457
.L456:
	movl	$1,-8(%ebp)
	jmp	.L458
.L457:
	movl	$0,-8(%ebp)
.L458:
	flds	.LC3
	fchs
	flds	-4(%ebp)
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L460
.L459:
	movl	$1,%edi
	jmp	.L461
.L460:
	movl	$0,%edi
.L461:
	fldz
	fchs
	flds	-4(%ebp)
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L463
.L462:
	movl	$1,%esi
	jmp	.L464
.L463:
	movl	$0,%esi
.L464:
	flds	-4(%ebp)
	fldz
	fxch
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L466
.L465:
	movl	$1,%ebx
	jmp	.L467
.L466:
	movl	$0,%ebx
.L467:
	flds	-4(%ebp)
	fcomps	.LC3
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L469
.L468:
	movl	$1,%edx
	jmp	.L470
.L469:
	movl	$0,%edx
.L470:
	flds	-4(%ebp)
	fcomps	.LC2
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L472
.L471:
	movl	$1,%ecx
	jmp	.L473
.L472:
	movl	$0,%ecx
.L473:
	flds	-4(%ebp)
	fcomps	.LC1
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L475
.L474:
	movl	$1,%eax
	jmp	.L476
.L475:
	movl	$0,%eax
.L476:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	%ebx
	pushl	%esi
	pushl	%edi
	pushl	-8(%ebp)
	pushl	-72(%ebp)
	pushl	$_s8
	call	_printf
	leal	36(%esp),%esp
.L477:
	popl	%edi
	popl	%esi
	popl	%ebx
	leave
	ret


	.align	4
	.global	_main2
_main2:
	pushl	%ebp
	movl	%esp,%ebp
	subl	$4,%esp
	flds	.LC4
	fstps	-4(%ebp)
	movl	$1,%edx
	flds	-4(%ebp)
	fcomps	.LC5
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L484
.L483:
	movl	$1,%ecx
	jmp	.L485
.L484:
	movl	$0,%ecx
.L485:
	flds	-4(%ebp)
	fcomps	.LC5
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L487
.L486:
	movl	$1,%eax
	jmp	.L488
.L487:
	movl	$0,%eax
.L488:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
	flds	-4(%ebp)
	fcomps	.LC4
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L490
.L489:
	movl	$1,%edx
	jmp	.L491
.L490:
	movl	$0,%edx
.L491:
	flds	-4(%ebp)
	fcomps	.LC5
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L493
.L492:
	movl	$1,%ecx
	jmp	.L494
.L493:
	movl	$0,%ecx
.L494:
	flds	-4(%ebp)
	fcomps	.LC5
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L496
.L495:
	movl	$1,%eax
	jmp	.L497
.L496:
	movl	$0,%eax
.L497:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
	flds	.LC5
	fstps	-4(%ebp)
	movl	$1,%edx
	flds	-4(%ebp)
	fcomps	.LC5
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L502
.L501:
	movl	$1,%ecx
	jmp	.L503
.L502:
	movl	$0,%ecx
.L503:
	flds	-4(%ebp)
	fcomps	.LC5
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L505
.L504:
	movl	$1,%eax
	jmp	.L506
.L505:
	movl	$0,%eax
.L506:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
	flds	-4(%ebp)
	fcomps	.LC4
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L508
.L507:
	movl	$1,%edx
	jmp	.L509
.L508:
	movl	$0,%edx
.L509:
	flds	-4(%ebp)
	fcomps	.LC5
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L511
.L510:
	movl	$1,%ecx
	jmp	.L512
.L511:
	movl	$0,%ecx
.L512:
	flds	-4(%ebp)
	fcomps	.LC5
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L514
.L513:
	movl	$1,%eax
	jmp	.L515
.L514:
	movl	$0,%eax
.L515:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
	flds	.LC5
	fstps	-4(%ebp)
	movl	$1,%edx
	flds	-4(%ebp)
	fcomps	.LC5
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L520
.L519:
	movl	$1,%ecx
	jmp	.L521
.L520:
	movl	$0,%ecx
.L521:
	flds	-4(%ebp)
	fcomps	.LC5
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L523
.L522:
	movl	$1,%eax
	jmp	.L524
.L523:
	movl	$0,%eax
.L524:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
	flds	-4(%ebp)
	fcomps	.LC4
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L526
.L525:
	movl	$1,%edx
	jmp	.L527
.L526:
	movl	$0,%edx
.L527:
	flds	-4(%ebp)
	fcomps	.LC5
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L529
.L528:
	movl	$1,%ecx
	jmp	.L530
.L529:
	movl	$0,%ecx
.L530:
	flds	-4(%ebp)
	fcomps	.LC5
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L532
.L531:
	movl	$1,%eax
	jmp	.L533
.L532:
	movl	$0,%eax
.L533:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
.L534:
	leave
	ret


	.align	4
	.global	_main3
_main3:
	pushl	%ebp
	movl	%esp,%ebp
	subl	$132,%esp
	pushl	%ebx
	pushl	%esi
	pushl	%edi
	flds	.LC1
	fchs
	fstps	-4(%ebp)
	flds	.LC1
	fchs
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L538
.L537:
	movl	$1,-132(%ebp)
	jmp	.L539
.L538:
	movl	$0,-132(%ebp)
.L539:
	flds	.LC2
	fchs
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L541
.L540:
	movl	$1,-68(%ebp)
	jmp	.L542
.L541:
	movl	$0,-68(%ebp)
.L542:
	flds	.LC3
	fchs
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L544
.L543:
	movl	$1,%edi
	jmp	.L545
.L544:
	movl	$0,%edi
.L545:
	fldz
	fchs
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L547
.L546:
	movl	$1,%esi
	jmp	.L548
.L547:
	movl	$0,%esi
.L548:
	fldz
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L550
.L549:
	movl	$1,%ebx
	jmp	.L551
.L550:
	movl	$0,%ebx
.L551:
	flds	.LC3
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L553
.L552:
	movl	$1,%edx
	jmp	.L554
.L553:
	movl	$0,%edx
.L554:
	flds	.LC2
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L556
.L555:
	movl	$1,%ecx
	jmp	.L557
.L556:
	movl	$0,%ecx
.L557:
	flds	.LC1
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L559
.L558:
	movl	$1,%eax
	jmp	.L560
.L559:
	movl	$0,%eax
.L560:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	%ebx
	pushl	%esi
	pushl	%edi
	pushl	-68(%ebp)
	pushl	-132(%ebp)
	pushl	$_s8
	call	_printf
	leal	36(%esp),%esp
	flds	.LC1
	fchs
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L562
.L561:
	movl	$1,-128(%ebp)
	jmp	.L563
.L562:
	movl	$0,-128(%ebp)
.L563:
	flds	.LC2
	fchs
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L565
.L564:
	movl	$1,-64(%ebp)
	jmp	.L566
.L565:
	movl	$0,-64(%ebp)
.L566:
	flds	.LC3
	fchs
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L568
.L567:
	movl	$1,%edi
	jmp	.L569
.L568:
	movl	$0,%edi
.L569:
	fldz
	fchs
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L571
.L570:
	movl	$1,%esi
	jmp	.L572
.L571:
	movl	$0,%esi
.L572:
	fldz
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L574
.L573:
	movl	$1,%ebx
	jmp	.L575
.L574:
	movl	$0,%ebx
.L575:
	flds	.LC3
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L577
.L576:
	movl	$1,%edx
	jmp	.L578
.L577:
	movl	$0,%edx
.L578:
	flds	.LC2
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L580
.L579:
	movl	$1,%ecx
	jmp	.L581
.L580:
	movl	$0,%ecx
.L581:
	flds	.LC1
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L583
.L582:
	movl	$1,%eax
	jmp	.L584
.L583:
	movl	$0,%eax
.L584:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	%ebx
	pushl	%esi
	pushl	%edi
	pushl	-64(%ebp)
	pushl	-128(%ebp)
	pushl	$_s8
	call	_printf
	leal	36(%esp),%esp
	flds	.LC2
	fchs
	fstps	-4(%ebp)
	flds	.LC1
	fchs
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L586
.L585:
	movl	$1,-124(%ebp)
	jmp	.L587
.L586:
	movl	$0,-124(%ebp)
.L587:
	flds	.LC2
	fchs
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L589
.L588:
	movl	$1,-60(%ebp)
	jmp	.L590
.L589:
	movl	$0,-60(%ebp)
.L590:
	flds	.LC3
	fchs
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L592
.L591:
	movl	$1,%edi
	jmp	.L593
.L592:
	movl	$0,%edi
.L593:
	fldz
	fchs
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L595
.L594:
	movl	$1,%esi
	jmp	.L596
.L595:
	movl	$0,%esi
.L596:
	fldz
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L598
.L597:
	movl	$1,%ebx
	jmp	.L599
.L598:
	movl	$0,%ebx
.L599:
	flds	.LC3
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L601
.L600:
	movl	$1,%edx
	jmp	.L602
.L601:
	movl	$0,%edx
.L602:
	flds	.LC2
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L604
.L603:
	movl	$1,%ecx
	jmp	.L605
.L604:
	movl	$0,%ecx
.L605:
	flds	.LC1
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L607
.L606:
	movl	$1,%eax
	jmp	.L608
.L607:
	movl	$0,%eax
.L608:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	%ebx
	pushl	%esi
	pushl	%edi
	pushl	-60(%ebp)
	pushl	-124(%ebp)
	pushl	$_s8
	call	_printf
	leal	36(%esp),%esp
	flds	.LC1
	fchs
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L610
.L609:
	movl	$1,-120(%ebp)
	jmp	.L611
.L610:
	movl	$0,-120(%ebp)
.L611:
	flds	.LC2
	fchs
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L613
.L612:
	movl	$1,-56(%ebp)
	jmp	.L614
.L613:
	movl	$0,-56(%ebp)
.L614:
	flds	.LC3
	fchs
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L616
.L615:
	movl	$1,%edi
	jmp	.L617
.L616:
	movl	$0,%edi
.L617:
	fldz
	fchs
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L619
.L618:
	movl	$1,%esi
	jmp	.L620
.L619:
	movl	$0,%esi
.L620:
	fldz
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L622
.L621:
	movl	$1,%ebx
	jmp	.L623
.L622:
	movl	$0,%ebx
.L623:
	flds	.LC3
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L625
.L624:
	movl	$1,%edx
	jmp	.L626
.L625:
	movl	$0,%edx
.L626:
	flds	.LC2
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L628
.L627:
	movl	$1,%ecx
	jmp	.L629
.L628:
	movl	$0,%ecx
.L629:
	flds	.LC1
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L631
.L630:
	movl	$1,%eax
	jmp	.L632
.L631:
	movl	$0,%eax
.L632:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	%ebx
	pushl	%esi
	pushl	%edi
	pushl	-56(%ebp)
	pushl	-120(%ebp)
	pushl	$_s8
	call	_printf
	leal	36(%esp),%esp
	flds	.LC3
	fchs
	fstps	-4(%ebp)
	flds	.LC1
	fchs
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L634
.L633:
	movl	$1,-116(%ebp)
	jmp	.L635
.L634:
	movl	$0,-116(%ebp)
.L635:
	flds	.LC2
	fchs
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L637
.L636:
	movl	$1,-52(%ebp)
	jmp	.L638
.L637:
	movl	$0,-52(%ebp)
.L638:
	flds	.LC3
	fchs
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L640
.L639:
	movl	$1,%edi
	jmp	.L641
.L640:
	movl	$0,%edi
.L641:
	fldz
	fchs
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L643
.L642:
	movl	$1,%esi
	jmp	.L644
.L643:
	movl	$0,%esi
.L644:
	fldz
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L646
.L645:
	movl	$1,%ebx
	jmp	.L647
.L646:
	movl	$0,%ebx
.L647:
	flds	.LC3
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L649
.L648:
	movl	$1,%edx
	jmp	.L650
.L649:
	movl	$0,%edx
.L650:
	flds	.LC2
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L652
.L651:
	movl	$1,%ecx
	jmp	.L653
.L652:
	movl	$0,%ecx
.L653:
	flds	.LC1
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L655
.L654:
	movl	$1,%eax
	jmp	.L656
.L655:
	movl	$0,%eax
.L656:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	%ebx
	pushl	%esi
	pushl	%edi
	pushl	-52(%ebp)
	pushl	-116(%ebp)
	pushl	$_s8
	call	_printf
	leal	36(%esp),%esp
	flds	.LC1
	fchs
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L658
.L657:
	movl	$1,-112(%ebp)
	jmp	.L659
.L658:
	movl	$0,-112(%ebp)
.L659:
	flds	.LC2
	fchs
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L661
.L660:
	movl	$1,-48(%ebp)
	jmp	.L662
.L661:
	movl	$0,-48(%ebp)
.L662:
	flds	.LC3
	fchs
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L664
.L663:
	movl	$1,%edi
	jmp	.L665
.L664:
	movl	$0,%edi
.L665:
	fldz
	fchs
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L667
.L666:
	movl	$1,%esi
	jmp	.L668
.L667:
	movl	$0,%esi
.L668:
	fldz
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L670
.L669:
	movl	$1,%ebx
	jmp	.L671
.L670:
	movl	$0,%ebx
.L671:
	flds	.LC3
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L673
.L672:
	movl	$1,%edx
	jmp	.L674
.L673:
	movl	$0,%edx
.L674:
	flds	.LC2
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L676
.L675:
	movl	$1,%ecx
	jmp	.L677
.L676:
	movl	$0,%ecx
.L677:
	flds	.LC1
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L679
.L678:
	movl	$1,%eax
	jmp	.L680
.L679:
	movl	$0,%eax
.L680:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	%ebx
	pushl	%esi
	pushl	%edi
	pushl	-48(%ebp)
	pushl	-112(%ebp)
	pushl	$_s8
	call	_printf
	leal	36(%esp),%esp
	fldz
	fchs
	fstps	-4(%ebp)
	flds	.LC1
	fchs
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L682
.L681:
	movl	$1,-108(%ebp)
	jmp	.L683
.L682:
	movl	$0,-108(%ebp)
.L683:
	flds	.LC2
	fchs
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L685
.L684:
	movl	$1,-44(%ebp)
	jmp	.L686
.L685:
	movl	$0,-44(%ebp)
.L686:
	flds	.LC3
	fchs
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L688
.L687:
	movl	$1,%edi
	jmp	.L689
.L688:
	movl	$0,%edi
.L689:
	fldz
	fchs
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L691
.L690:
	movl	$1,%esi
	jmp	.L692
.L691:
	movl	$0,%esi
.L692:
	fldz
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L694
.L693:
	movl	$1,%ebx
	jmp	.L695
.L694:
	movl	$0,%ebx
.L695:
	flds	.LC3
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L697
.L696:
	movl	$1,%edx
	jmp	.L698
.L697:
	movl	$0,%edx
.L698:
	flds	.LC2
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L700
.L699:
	movl	$1,%ecx
	jmp	.L701
.L700:
	movl	$0,%ecx
.L701:
	flds	.LC1
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L703
.L702:
	movl	$1,%eax
	jmp	.L704
.L703:
	movl	$0,%eax
.L704:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	%ebx
	pushl	%esi
	pushl	%edi
	pushl	-44(%ebp)
	pushl	-108(%ebp)
	pushl	$_s8
	call	_printf
	leal	36(%esp),%esp
	flds	.LC1
	fchs
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L706
.L705:
	movl	$1,-104(%ebp)
	jmp	.L707
.L706:
	movl	$0,-104(%ebp)
.L707:
	flds	.LC2
	fchs
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L709
.L708:
	movl	$1,-40(%ebp)
	jmp	.L710
.L709:
	movl	$0,-40(%ebp)
.L710:
	flds	.LC3
	fchs
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L712
.L711:
	movl	$1,%edi
	jmp	.L713
.L712:
	movl	$0,%edi
.L713:
	fldz
	fchs
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L715
.L714:
	movl	$1,%esi
	jmp	.L716
.L715:
	movl	$0,%esi
.L716:
	fldz
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L718
.L717:
	movl	$1,%ebx
	jmp	.L719
.L718:
	movl	$0,%ebx
.L719:
	flds	.LC3
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L721
.L720:
	movl	$1,%edx
	jmp	.L722
.L721:
	movl	$0,%edx
.L722:
	flds	.LC2
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L724
.L723:
	movl	$1,%ecx
	jmp	.L725
.L724:
	movl	$0,%ecx
.L725:
	flds	.LC1
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L727
.L726:
	movl	$1,%eax
	jmp	.L728
.L727:
	movl	$0,%eax
.L728:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	%ebx
	pushl	%esi
	pushl	%edi
	pushl	-40(%ebp)
	pushl	-104(%ebp)
	pushl	$_s8
	call	_printf
	leal	36(%esp),%esp
	fldz
	fstps	-4(%ebp)
	flds	.LC1
	fchs
	fldz
	fxch
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L730
.L729:
	movl	$1,-100(%ebp)
	jmp	.L731
.L730:
	movl	$0,-100(%ebp)
.L731:
	flds	.LC2
	fchs
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L733
.L732:
	movl	$1,-36(%ebp)
	jmp	.L734
.L733:
	movl	$0,-36(%ebp)
.L734:
	flds	.LC3
	fchs
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L736
.L735:
	movl	$1,%edi
	jmp	.L737
.L736:
	movl	$0,%edi
.L737:
	fldz
	fchs
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L739
.L738:
	movl	$1,%esi
	jmp	.L740
.L739:
	movl	$0,%esi
.L740:
	fldz
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L742
.L741:
	movl	$1,%ebx
	jmp	.L743
.L742:
	movl	$0,%ebx
.L743:
	flds	.LC3
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L745
.L744:
	movl	$1,%edx
	jmp	.L746
.L745:
	movl	$0,%edx
.L746:
	flds	.LC2
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L748
.L747:
	movl	$1,%ecx
	jmp	.L749
.L748:
	movl	$0,%ecx
.L749:
	flds	.LC1
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L751
.L750:
	movl	$1,%eax
	jmp	.L752
.L751:
	movl	$0,%eax
.L752:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	%ebx
	pushl	%esi
	pushl	%edi
	pushl	-36(%ebp)
	pushl	-100(%ebp)
	pushl	$_s8
	call	_printf
	leal	36(%esp),%esp
	flds	.LC1
	fchs
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L754
.L753:
	movl	$1,-96(%ebp)
	jmp	.L755
.L754:
	movl	$0,-96(%ebp)
.L755:
	flds	.LC2
	fchs
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L757
.L756:
	movl	$1,-32(%ebp)
	jmp	.L758
.L757:
	movl	$0,-32(%ebp)
.L758:
	flds	.LC3
	fchs
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L760
.L759:
	movl	$1,%edi
	jmp	.L761
.L760:
	movl	$0,%edi
.L761:
	fldz
	fchs
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L763
.L762:
	movl	$1,%esi
	jmp	.L764
.L763:
	movl	$0,%esi
.L764:
	fldz
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L766
.L765:
	movl	$1,%ebx
	jmp	.L767
.L766:
	movl	$0,%ebx
.L767:
	flds	.LC3
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L769
.L768:
	movl	$1,%edx
	jmp	.L770
.L769:
	movl	$0,%edx
.L770:
	flds	.LC2
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L772
.L771:
	movl	$1,%ecx
	jmp	.L773
.L772:
	movl	$0,%ecx
.L773:
	flds	.LC1
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L775
.L774:
	movl	$1,%eax
	jmp	.L776
.L775:
	movl	$0,%eax
.L776:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	%ebx
	pushl	%esi
	pushl	%edi
	pushl	-32(%ebp)
	pushl	-96(%ebp)
	pushl	$_s8
	call	_printf
	leal	36(%esp),%esp
	flds	.LC3
	fstps	-4(%ebp)
	flds	.LC1
	fchs
	fcomps	.LC3
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L778
.L777:
	movl	$1,-92(%ebp)
	jmp	.L779
.L778:
	movl	$0,-92(%ebp)
.L779:
	flds	.LC2
	fchs
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L781
.L780:
	movl	$1,-28(%ebp)
	jmp	.L782
.L781:
	movl	$0,-28(%ebp)
.L782:
	flds	.LC3
	fchs
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L784
.L783:
	movl	$1,%edi
	jmp	.L785
.L784:
	movl	$0,%edi
.L785:
	fldz
	fchs
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L787
.L786:
	movl	$1,%esi
	jmp	.L788
.L787:
	movl	$0,%esi
.L788:
	fldz
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L790
.L789:
	movl	$1,%ebx
	jmp	.L791
.L790:
	movl	$0,%ebx
.L791:
	flds	.LC3
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L793
.L792:
	movl	$1,%edx
	jmp	.L794
.L793:
	movl	$0,%edx
.L794:
	flds	.LC2
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L796
.L795:
	movl	$1,%ecx
	jmp	.L797
.L796:
	movl	$0,%ecx
.L797:
	flds	.LC1
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L799
.L798:
	movl	$1,%eax
	jmp	.L800
.L799:
	movl	$0,%eax
.L800:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	%ebx
	pushl	%esi
	pushl	%edi
	pushl	-28(%ebp)
	pushl	-92(%ebp)
	pushl	$_s8
	call	_printf
	leal	36(%esp),%esp
	flds	.LC1
	fchs
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L802
.L801:
	movl	$1,-88(%ebp)
	jmp	.L803
.L802:
	movl	$0,-88(%ebp)
.L803:
	flds	.LC2
	fchs
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L805
.L804:
	movl	$1,-24(%ebp)
	jmp	.L806
.L805:
	movl	$0,-24(%ebp)
.L806:
	flds	.LC3
	fchs
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L808
.L807:
	movl	$1,%edi
	jmp	.L809
.L808:
	movl	$0,%edi
.L809:
	fldz
	fchs
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L811
.L810:
	movl	$1,%esi
	jmp	.L812
.L811:
	movl	$0,%esi
.L812:
	fldz
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L814
.L813:
	movl	$1,%ebx
	jmp	.L815
.L814:
	movl	$0,%ebx
.L815:
	flds	.LC3
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L817
.L816:
	movl	$1,%edx
	jmp	.L818
.L817:
	movl	$0,%edx
.L818:
	flds	.LC2
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L820
.L819:
	movl	$1,%ecx
	jmp	.L821
.L820:
	movl	$0,%ecx
.L821:
	flds	.LC1
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L823
.L822:
	movl	$1,%eax
	jmp	.L824
.L823:
	movl	$0,%eax
.L824:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	%ebx
	pushl	%esi
	pushl	%edi
	pushl	-24(%ebp)
	pushl	-88(%ebp)
	pushl	$_s8
	call	_printf
	leal	36(%esp),%esp
	flds	.LC2
	fstps	-4(%ebp)
	flds	.LC1
	fchs
	fcomps	.LC2
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L826
.L825:
	movl	$1,-84(%ebp)
	jmp	.L827
.L826:
	movl	$0,-84(%ebp)
.L827:
	flds	.LC2
	fchs
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L829
.L828:
	movl	$1,-20(%ebp)
	jmp	.L830
.L829:
	movl	$0,-20(%ebp)
.L830:
	flds	.LC3
	fchs
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L832
.L831:
	movl	$1,%edi
	jmp	.L833
.L832:
	movl	$0,%edi
.L833:
	fldz
	fchs
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L835
.L834:
	movl	$1,%esi
	jmp	.L836
.L835:
	movl	$0,%esi
.L836:
	fldz
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L838
.L837:
	movl	$1,%ebx
	jmp	.L839
.L838:
	movl	$0,%ebx
.L839:
	flds	.LC3
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L841
.L840:
	movl	$1,%edx
	jmp	.L842
.L841:
	movl	$0,%edx
.L842:
	flds	.LC2
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L844
.L843:
	movl	$1,%ecx
	jmp	.L845
.L844:
	movl	$0,%ecx
.L845:
	flds	.LC1
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L847
.L846:
	movl	$1,%eax
	jmp	.L848
.L847:
	movl	$0,%eax
.L848:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	%ebx
	pushl	%esi
	pushl	%edi
	pushl	-20(%ebp)
	pushl	-84(%ebp)
	pushl	$_s8
	call	_printf
	leal	36(%esp),%esp
	flds	.LC1
	fchs
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L850
.L849:
	movl	$1,-80(%ebp)
	jmp	.L851
.L850:
	movl	$0,-80(%ebp)
.L851:
	flds	.LC2
	fchs
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L853
.L852:
	movl	$1,-16(%ebp)
	jmp	.L854
.L853:
	movl	$0,-16(%ebp)
.L854:
	flds	.LC3
	fchs
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L856
.L855:
	movl	$1,%edi
	jmp	.L857
.L856:
	movl	$0,%edi
.L857:
	fldz
	fchs
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L859
.L858:
	movl	$1,%esi
	jmp	.L860
.L859:
	movl	$0,%esi
.L860:
	fldz
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L862
.L861:
	movl	$1,%ebx
	jmp	.L863
.L862:
	movl	$0,%ebx
.L863:
	flds	.LC3
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L865
.L864:
	movl	$1,%edx
	jmp	.L866
.L865:
	movl	$0,%edx
.L866:
	flds	.LC2
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L868
.L867:
	movl	$1,%ecx
	jmp	.L869
.L868:
	movl	$0,%ecx
.L869:
	flds	.LC1
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L871
.L870:
	movl	$1,%eax
	jmp	.L872
.L871:
	movl	$0,%eax
.L872:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	%ebx
	pushl	%esi
	pushl	%edi
	pushl	-16(%ebp)
	pushl	-80(%ebp)
	pushl	$_s8
	call	_printf
	leal	36(%esp),%esp
	flds	.LC1
	fstps	-4(%ebp)
	flds	.LC1
	fchs
	fcomps	.LC1
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L874
.L873:
	movl	$1,-76(%ebp)
	jmp	.L875
.L874:
	movl	$0,-76(%ebp)
.L875:
	flds	.LC2
	fchs
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L877
.L876:
	movl	$1,-12(%ebp)
	jmp	.L878
.L877:
	movl	$0,-12(%ebp)
.L878:
	flds	.LC3
	fchs
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L880
.L879:
	movl	$1,%edi
	jmp	.L881
.L880:
	movl	$0,%edi
.L881:
	fldz
	fchs
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L883
.L882:
	movl	$1,%esi
	jmp	.L884
.L883:
	movl	$0,%esi
.L884:
	fldz
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L886
.L885:
	movl	$1,%ebx
	jmp	.L887
.L886:
	movl	$0,%ebx
.L887:
	flds	.LC3
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L889
.L888:
	movl	$1,%edx
	jmp	.L890
.L889:
	movl	$0,%edx
.L890:
	flds	.LC2
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L892
.L891:
	movl	$1,%ecx
	jmp	.L893
.L892:
	movl	$0,%ecx
.L893:
	flds	.LC1
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L895
.L894:
	movl	$1,%eax
	jmp	.L896
.L895:
	movl	$0,%eax
.L896:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	%ebx
	pushl	%esi
	pushl	%edi
	pushl	-12(%ebp)
	pushl	-76(%ebp)
	pushl	$_s8
	call	_printf
	leal	36(%esp),%esp
	flds	.LC1
	fchs
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L898
.L897:
	movl	$1,-72(%ebp)
	jmp	.L899
.L898:
	movl	$0,-72(%ebp)
.L899:
	flds	.LC2
	fchs
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L901
.L900:
	movl	$1,-8(%ebp)
	jmp	.L902
.L901:
	movl	$0,-8(%ebp)
.L902:
	flds	.LC3
	fchs
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L904
.L903:
	movl	$1,%edi
	jmp	.L905
.L904:
	movl	$0,%edi
.L905:
	fldz
	fchs
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L907
.L906:
	movl	$1,%esi
	jmp	.L908
.L907:
	movl	$0,%esi
.L908:
	fldz
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L910
.L909:
	movl	$1,%ebx
	jmp	.L911
.L910:
	movl	$0,%ebx
.L911:
	flds	.LC3
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L913
.L912:
	movl	$1,%edx
	jmp	.L914
.L913:
	movl	$0,%edx
.L914:
	flds	.LC2
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L916
.L915:
	movl	$1,%ecx
	jmp	.L917
.L916:
	movl	$0,%ecx
.L917:
	flds	.LC1
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L919
.L918:
	movl	$1,%eax
	jmp	.L920
.L919:
	movl	$0,%eax
.L920:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	%ebx
	pushl	%esi
	pushl	%edi
	pushl	-8(%ebp)
	pushl	-72(%ebp)
	pushl	$_s8
	call	_printf
	leal	36(%esp),%esp
.L921:
	popl	%edi
	popl	%esi
	popl	%ebx
	leave
	ret


	.align	4
	.global	_main4
_main4:
	pushl	%ebp
	movl	%esp,%ebp
	subl	$4,%esp
	flds	.LC4
	fstps	-4(%ebp)
	movl	$1,%edx
	flds	.LC5
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L928
.L927:
	movl	$1,%ecx
	jmp	.L929
.L928:
	movl	$0,%ecx
.L929:
	flds	.LC5
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L931
.L930:
	movl	$1,%eax
	jmp	.L932
.L931:
	movl	$0,%eax
.L932:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
	flds	.LC4
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L934
.L933:
	movl	$1,%edx
	jmp	.L935
.L934:
	movl	$0,%edx
.L935:
	flds	.LC5
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L937
.L936:
	movl	$1,%ecx
	jmp	.L938
.L937:
	movl	$0,%ecx
.L938:
	flds	.LC5
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L940
.L939:
	movl	$1,%eax
	jmp	.L941
.L940:
	movl	$0,%eax
.L941:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
	flds	.LC5
	fstps	-4(%ebp)
	movl	$0,%edx
	flds	.LC5
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L946
.L945:
	movl	$1,%ecx
	jmp	.L947
.L946:
	movl	$0,%ecx
.L947:
	flds	.LC5
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L949
.L948:
	movl	$1,%eax
	jmp	.L950
.L949:
	movl	$0,%eax
.L950:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
	flds	.LC4
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L952
.L951:
	movl	$1,%edx
	jmp	.L953
.L952:
	movl	$0,%edx
.L953:
	flds	.LC5
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L955
.L954:
	movl	$1,%ecx
	jmp	.L956
.L955:
	movl	$0,%ecx
.L956:
	flds	.LC5
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L958
.L957:
	movl	$1,%eax
	jmp	.L959
.L958:
	movl	$0,%eax
.L959:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
	flds	.LC5
	fstps	-4(%ebp)
	movl	$0,%edx
	flds	.LC5
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L964
.L963:
	movl	$1,%ecx
	jmp	.L965
.L964:
	movl	$0,%ecx
.L965:
	flds	.LC5
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L967
.L966:
	movl	$1,%eax
	jmp	.L968
.L967:
	movl	$0,%eax
.L968:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
	flds	.LC4
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L970
.L969:
	movl	$1,%edx
	jmp	.L971
.L970:
	movl	$0,%edx
.L971:
	flds	.LC5
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L973
.L972:
	movl	$1,%ecx
	jmp	.L974
.L973:
	movl	$0,%ecx
.L974:
	flds	.LC5
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	je	.L976
.L975:
	movl	$1,%eax
	jmp	.L977
.L976:
	movl	$0,%eax
.L977:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
.L978:
	leave
	ret


	.align	4
	.global	_main
_main:
	pushl	%ebp
	movl	%esp,%ebp
	pushl	_a07
	pushl	_a06
	pushl	_a05
	pushl	_a04
	pushl	_a03
	pushl	_a02
	pushl	_a01
	pushl	_a00
	pushl	$_s8
	call	_printf
	leal	36(%esp),%esp
	pushl	_a17
	pushl	_a16
	pushl	_a15
	pushl	_a14
	pushl	_a13
	pushl	_a12
	pushl	_a11
	pushl	_a10
	pushl	$_s8
	call	_printf
	leal	36(%esp),%esp
	pushl	_a27
	pushl	_a26
	pushl	_a25
	pushl	_a24
	pushl	_a23
	pushl	_a22
	pushl	_a21
	pushl	_a20
	pushl	$_s8
	call	_printf
	leal	36(%esp),%esp
	pushl	_a37
	pushl	_a36
	pushl	_a35
	pushl	_a34
	pushl	_a33
	pushl	_a32
	pushl	_a31
	pushl	_a30
	pushl	$_s8
	call	_printf
	leal	36(%esp),%esp
	pushl	_a47
	pushl	_a46
	pushl	_a45
	pushl	_a44
	pushl	_a43
	pushl	_a42
	pushl	_a41
	pushl	_a40
	pushl	$_s8
	call	_printf
	leal	36(%esp),%esp
	pushl	_a57
	pushl	_a56
	pushl	_a55
	pushl	_a54
	pushl	_a53
	pushl	_a52
	pushl	_a51
	pushl	_a50
	pushl	$_s8
	call	_printf
	leal	36(%esp),%esp
	pushl	_a67
	pushl	_a66
	pushl	_a65
	pushl	_a64
	pushl	_a63
	pushl	_a62
	pushl	_a61
	pushl	_a60
	pushl	$_s8
	call	_printf
	leal	36(%esp),%esp
	pushl	_a77
	pushl	_a76
	pushl	_a75
	pushl	_a74
	pushl	_a73
	pushl	_a72
	pushl	_a71
	pushl	_a70
	pushl	$_s8
	call	_printf
	leal	36(%esp),%esp
	pushl	_b02
	pushl	_b01
	pushl	_b00
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
	pushl	_b12
	pushl	_b11
	pushl	_b10
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
	pushl	_b22
	pushl	_b21
	pushl	_b20
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
	call	_main1
	call	_main2
	call	_main3
	call	_main4
	flds	.LC1
	fchs
	sub	$4,%esp
	fstps	(%esp)
	call	_f0
	leal	4(%esp),%esp
	flds	.LC2
	fchs
	sub	$4,%esp
	fstps	(%esp)
	call	_f0
	leal	4(%esp),%esp
	flds	.LC3
	fchs
	sub	$4,%esp
	fstps	(%esp)
	call	_f0
	leal	4(%esp),%esp
	fldz
	fchs
	sub	$4,%esp
	fstps	(%esp)
	call	_f0
	leal	4(%esp),%esp
	fldz
	sub	$4,%esp
	fstps	(%esp)
	call	_f0
	leal	4(%esp),%esp
	pushl	.LC3
	call	_f0
	leal	4(%esp),%esp
	pushl	.LC2
	call	_f0
	leal	4(%esp),%esp
	pushl	.LC1
	call	_f0
	leal	4(%esp),%esp
	pushl	.LC4
	call	_g0
	leal	4(%esp),%esp
	pushl	.LC5
	call	_g0
	leal	4(%esp),%esp
	pushl	.LC5
	call	_g0
	leal	4(%esp),%esp
	flds	.LC1
	fchs
	sub	$4,%esp
	fstps	(%esp)
	call	_f1
	leal	4(%esp),%esp
	flds	.LC2
	fchs
	sub	$4,%esp
	fstps	(%esp)
	call	_f1
	leal	4(%esp),%esp
	flds	.LC3
	fchs
	sub	$4,%esp
	fstps	(%esp)
	call	_f1
	leal	4(%esp),%esp
	fldz
	fchs
	sub	$4,%esp
	fstps	(%esp)
	call	_f1
	leal	4(%esp),%esp
	fldz
	sub	$4,%esp
	fstps	(%esp)
	call	_f1
	leal	4(%esp),%esp
	pushl	.LC3
	call	_f1
	leal	4(%esp),%esp
	pushl	.LC2
	call	_f1
	leal	4(%esp),%esp
	pushl	.LC1
	call	_f1
	leal	4(%esp),%esp
	pushl	.LC4
	call	_g1
	leal	4(%esp),%esp
	pushl	.LC5
	call	_g1
	leal	4(%esp),%esp
	pushl	.LC5
	call	_g1
	leal	4(%esp),%esp
	flds	.LC1
	fchs
	sub	$4,%esp
	fstps	(%esp)
	call	_f2
	leal	4(%esp),%esp
	flds	.LC2
	fchs
	sub	$4,%esp
	fstps	(%esp)
	call	_f2
	leal	4(%esp),%esp
	flds	.LC3
	fchs
	sub	$4,%esp
	fstps	(%esp)
	call	_f2
	leal	4(%esp),%esp
	fldz
	fchs
	sub	$4,%esp
	fstps	(%esp)
	call	_f2
	leal	4(%esp),%esp
	fldz
	sub	$4,%esp
	fstps	(%esp)
	call	_f2
	leal	4(%esp),%esp
	pushl	.LC3
	call	_f2
	leal	4(%esp),%esp
	pushl	.LC2
	call	_f2
	leal	4(%esp),%esp
	pushl	.LC1
	call	_f2
	leal	4(%esp),%esp
	pushl	.LC4
	call	_g2
	leal	4(%esp),%esp
	pushl	.LC5
	call	_g2
	leal	4(%esp),%esp
	pushl	.LC5
	call	_g2
	leal	4(%esp),%esp
	movl	$0,%eax
	leave
	ret

	.data
	.align	1
	.global	_s3
_s3:
	.byte	37
	.byte	100
	.byte	32
	.byte	37
	.byte	100
	.byte	32
	.byte	37
	.byte	100
	.byte	10
	.byte	0
	.align	1
	.global	_s8
_s8:
	.byte	37
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
	.byte	37
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
	.byte	10
	.byte	0
	.align	4
	.global	_a00
_a00:
	.long	1
	.align	4
	.global	_a01
_a01:
	.long	0
	.align	4
	.global	_a02
_a02:
	.long	0
	.align	4
	.global	_a03
_a03:
	.long	0
	.align	4
	.global	_a04
_a04:
	.long	0
	.align	4
	.global	_a05
_a05:
	.long	0
	.align	4
	.global	_a06
_a06:
	.long	0
	.align	4
	.global	_a07
_a07:
	.long	0
	.align	4
	.global	_a10
_a10:
	.long	1
	.align	4
	.global	_a11
_a11:
	.long	1
	.align	4
	.global	_a12
_a12:
	.long	0
	.align	4
	.global	_a13
_a13:
	.long	0
	.align	4
	.global	_a14
_a14:
	.long	0
	.align	4
	.global	_a15
_a15:
	.long	0
	.align	4
	.global	_a16
_a16:
	.long	0
	.align	4
	.global	_a17
_a17:
	.long	0
	.align	4
	.global	_a20
_a20:
	.long	1
	.align	4
	.global	_a21
_a21:
	.long	1
	.align	4
	.global	_a22
_a22:
	.long	1
	.align	4
	.global	_a23
_a23:
	.long	0
	.align	4
	.global	_a24
_a24:
	.long	0
	.align	4
	.global	_a25
_a25:
	.long	0
	.align	4
	.global	_a26
_a26:
	.long	0
	.align	4
	.global	_a27
_a27:
	.long	0
	.align	4
	.global	_a30
_a30:
	.long	1
	.align	4
	.global	_a31
_a31:
	.long	1
	.align	4
	.global	_a32
_a32:
	.long	1
	.align	4
	.global	_a33
_a33:
	.long	1
	.align	4
	.global	_a34
_a34:
	.long	1
	.align	4
	.global	_a35
_a35:
	.long	0
	.align	4
	.global	_a36
_a36:
	.long	0
	.align	4
	.global	_a37
_a37:
	.long	0
	.align	4
	.global	_a40
_a40:
	.long	1
	.align	4
	.global	_a41
_a41:
	.long	1
	.align	4
	.global	_a42
_a42:
	.long	1
	.align	4
	.global	_a43
_a43:
	.long	1
	.align	4
	.global	_a44
_a44:
	.long	1
	.align	4
	.global	_a45
_a45:
	.long	0
	.align	4
	.global	_a46
_a46:
	.long	0
	.align	4
	.global	_a47
_a47:
	.long	0
	.align	4
	.global	_a50
_a50:
	.long	1
	.align	4
	.global	_a51
_a51:
	.long	1
	.align	4
	.global	_a52
_a52:
	.long	1
	.align	4
	.global	_a53
_a53:
	.long	1
	.align	4
	.global	_a54
_a54:
	.long	1
	.align	4
	.global	_a55
_a55:
	.long	1
	.align	4
	.global	_a56
_a56:
	.long	0
	.align	4
	.global	_a57
_a57:
	.long	0
	.align	4
	.global	_a60
_a60:
	.long	1
	.align	4
	.global	_a61
_a61:
	.long	1
	.align	4
	.global	_a62
_a62:
	.long	1
	.align	4
	.global	_a63
_a63:
	.long	1
	.align	4
	.global	_a64
_a64:
	.long	1
	.align	4
	.global	_a65
_a65:
	.long	1
	.align	4
	.global	_a66
_a66:
	.long	1
	.align	4
	.global	_a67
_a67:
	.long	0
	.align	4
	.global	_a70
_a70:
	.long	1
	.align	4
	.global	_a71
_a71:
	.long	1
	.align	4
	.global	_a72
_a72:
	.long	1
	.align	4
	.global	_a73
_a73:
	.long	1
	.align	4
	.global	_a74
_a74:
	.long	1
	.align	4
	.global	_a75
_a75:
	.long	1
	.align	4
	.global	_a76
_a76:
	.long	1
	.align	4
	.global	_a77
_a77:
	.long	1
	.align	4
	.global	_b00
_b00:
	.long	1
	.align	4
	.global	_b01
_b01:
	.long	0
	.align	4
	.global	_b02
_b02:
	.long	0
	.align	4
	.global	_b10
_b10:
	.long	1
	.align	4
	.global	_b11
_b11:
	.long	1
	.align	4
	.global	_b12
_b12:
	.long	1
	.align	4
	.global	_b20
_b20:
	.long	1
	.align	4
	.global	_b21
_b21:
	.long	1
	.align	4
	.global	_b22
_b22:
	.long	1
	.text
	.align	4
.LC1:
	.long	0x7f7fffff /* 3.4028234663852886E38 */
	.align	4
.LC2:
	.long	0x402ccccd /* 2.700000047683716 */
	.align	4
.LC3:
	.long	0x800000 /* 1.1754943508222875E-38 */
	.align	4
.LC4:
	.long	0x402df84d /* 2.718280076980591 */
	.align	4
.LC5:
	.long	0x402df854 /* 2.7182817459106445 */
