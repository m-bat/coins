 .ident "Coins Compiler version: coins-1.4.3.1 + BackEnd-1.0"
/* JavaCG for target:x86 convention:cygwin */

	.text
	.align	4
	.global	_f0
_f0:
	pushl	%ebp
	movl	%esp,%ebp
	movl	8(%ebp),%eax
	cmpl	$_s,%eax
	je	.L4
.L3:
	movl	$1,%edx
	jmp	.L5
.L4:
	movl	$0,%edx
.L5:
	cmpl	$_s+4,%eax
	je	.L7
.L6:
	movl	$1,%ecx
	jmp	.L8
.L7:
	movl	$0,%ecx
.L8:
	cmpl	$0,%eax
	je	.L10
.L9:
	movl	$1,%eax
	jmp	.L11
.L10:
	movl	$0,%eax
.L11:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
.L12:
	leave
	ret


	.align	4
	.global	_g0
_g0:
	pushl	%ebp
	movl	%esp,%ebp
	movl	8(%ebp),%eax
	cmpl	$_u,%eax
	je	.L16
.L15:
	movl	$1,%edx
	jmp	.L17
.L16:
	movl	$0,%edx
.L17:
	cmpl	$_u,%eax
	je	.L19
.L18:
	movl	$1,%ecx
	jmp	.L20
.L19:
	movl	$0,%ecx
.L20:
	cmpl	$0,%eax
	je	.L22
.L21:
	movl	$1,%eax
	jmp	.L23
.L22:
	movl	$0,%eax
.L23:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
.L24:
	leave
	ret


	.align	4
	.global	_h0
_h0:
	pushl	%ebp
	movl	%esp,%ebp
	movl	8(%ebp),%eax
	cmpl	$_a+8,%eax
	je	.L28
.L27:
	movl	$1,%edx
	jmp	.L29
.L28:
	movl	$0,%edx
.L29:
	cmpl	$_a+16,%eax
	je	.L31
.L30:
	movl	$1,%ecx
	jmp	.L32
.L31:
	movl	$0,%ecx
.L32:
	cmpl	$0,%eax
	je	.L34
.L33:
	movl	$1,%eax
	jmp	.L35
.L34:
	movl	$0,%eax
.L35:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
.L36:
	leave
	ret


	.align	4
	.global	_f1
_f1:
	pushl	%ebp
	movl	%esp,%ebp
	movl	8(%ebp),%eax
	cmpl	$_s,%eax
	je	.L40
.L39:
	movl	$1,%edx
	jmp	.L41
.L40:
	movl	$0,%edx
.L41:
	cmpl	$_s+4,%eax
	je	.L43
.L42:
	movl	$1,%ecx
	jmp	.L44
.L43:
	movl	$0,%ecx
.L44:
	cmpl	$0,%eax
	je	.L46
.L45:
	movl	$1,%eax
	jmp	.L47
.L46:
	movl	$0,%eax
.L47:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
.L48:
	leave
	ret


	.align	4
	.global	_g1
_g1:
	pushl	%ebp
	movl	%esp,%ebp
	movl	8(%ebp),%eax
	cmpl	$_u,%eax
	je	.L52
.L51:
	movl	$1,%edx
	jmp	.L53
.L52:
	movl	$0,%edx
.L53:
	cmpl	$_u,%eax
	je	.L55
.L54:
	movl	$1,%ecx
	jmp	.L56
.L55:
	movl	$0,%ecx
.L56:
	cmpl	$0,%eax
	je	.L58
.L57:
	movl	$1,%eax
	jmp	.L59
.L58:
	movl	$0,%eax
.L59:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
.L60:
	leave
	ret


	.align	4
	.global	_h1
_h1:
	pushl	%ebp
	movl	%esp,%ebp
	movl	8(%ebp),%eax
	cmpl	$_a+8,%eax
	je	.L64
.L63:
	movl	$1,%edx
	jmp	.L65
.L64:
	movl	$0,%edx
.L65:
	cmpl	$_a+16,%eax
	je	.L67
.L66:
	movl	$1,%ecx
	jmp	.L68
.L67:
	movl	$0,%ecx
.L68:
	cmpl	$0,%eax
	je	.L70
.L69:
	movl	$1,%eax
	jmp	.L71
.L70:
	movl	$0,%eax
.L71:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
.L72:
	leave
	ret


	.align	4
	.global	_op
_op:
	pushl	%ebp
	movl	%esp,%ebp
	movl	8(%ebp),%ecx
	movl	12(%ebp),%eax
	cmpl	%eax,%ecx
	je	.L76
.L75:
	movl	$1,%eax
	jmp	.L77
.L76:
	movl	$0,%eax
.L77:
	leave
	ret


	.align	4
	.global	_f2
_f2:
	pushl	%ebp
	movl	%esp,%ebp
	pushl	%ebx
	pushl	%esi
	pushl	%edi
	movl	8(%ebp),%edi
	pushl	$_s
	pushl	%edi
	call	_op
	movl	%eax,%esi
	leal	8(%esp),%esp
	pushl	$_s+4
	pushl	%edi
	call	_op
	movl	%eax,%ebx
	leal	8(%esp),%esp
	pushl	$0
	pushl	%edi
	call	_op
	leal	8(%esp),%esp
	pushl	%eax
	pushl	%ebx
	pushl	%esi
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
.L81:
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
	pushl	%ebx
	pushl	%esi
	pushl	%edi
	movl	8(%ebp),%edi
	pushl	$_u
	pushl	%edi
	call	_op
	movl	%eax,%esi
	leal	8(%esp),%esp
	pushl	$_u
	pushl	%edi
	call	_op
	movl	%eax,%ebx
	leal	8(%esp),%esp
	pushl	$0
	pushl	%edi
	call	_op
	leal	8(%esp),%esp
	pushl	%eax
	pushl	%ebx
	pushl	%esi
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
.L84:
	popl	%edi
	popl	%esi
	popl	%ebx
	leave
	ret


	.align	4
	.global	_h2
_h2:
	pushl	%ebp
	movl	%esp,%ebp
	pushl	%ebx
	pushl	%esi
	pushl	%edi
	movl	8(%ebp),%edi
	pushl	$_a+8
	pushl	%edi
	call	_op
	movl	%eax,%esi
	leal	8(%esp),%esp
	pushl	$_a+16
	pushl	%edi
	call	_op
	movl	%eax,%ebx
	leal	8(%esp),%esp
	pushl	$0
	pushl	%edi
	call	_op
	leal	8(%esp),%esp
	pushl	%eax
	pushl	%ebx
	pushl	%esi
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
.L87:
	popl	%edi
	popl	%esi
	popl	%ebx
	leave
	ret


	.align	4
	.global	_main0g
_main0g:
	pushl	%ebp
	movl	%esp,%ebp
	subl	$100,%esp
	pushl	%ebx
	pushl	%esi
	pushl	%edi
	movl	$_s,%eax
	cmpl	$_s,%eax
	je	.L91
.L90:
	movl	$1,-96(%ebp)
	jmp	.L92
.L91:
	movl	$0,-96(%ebp)
.L92:
	movl	$_s,%eax
	cmpl	$_s+4,%eax
	je	.L94
.L93:
	movl	$1,%edx
	jmp	.L95
.L94:
	movl	$0,%edx
.L95:
	movl	$_s,%eax
	cmpl	$0,%eax
	je	.L97
.L96:
	movl	$1,%ecx
	jmp	.L98
.L97:
	movl	$0,%ecx
.L98:
	movl	$_s+4,%eax
	cmpl	$_s,%eax
	je	.L100
.L99:
	movl	$1,-100(%ebp)
	jmp	.L101
.L100:
	movl	$0,-100(%ebp)
.L101:
	movl	$_s+4,%eax
	cmpl	$_s+4,%eax
	je	.L103
.L102:
	movl	$1,-88(%ebp)
	jmp	.L104
.L103:
	movl	$0,-88(%ebp)
.L104:
	movl	$_s+4,%eax
	cmpl	$0,%eax
	je	.L106
.L105:
	movl	$1,-72(%ebp)
	jmp	.L107
.L106:
	movl	$0,-72(%ebp)
.L107:
	movl	$0,%eax
	cmpl	$_s,%eax
	je	.L109
.L108:
	movl	$1,-92(%ebp)
	jmp	.L110
.L109:
	movl	$0,-92(%ebp)
.L110:
	movl	$0,%eax
	cmpl	$_s+4,%eax
	je	.L112
.L111:
	movl	$1,-80(%ebp)
	jmp	.L115
.L112:
	movl	$0,-80(%ebp)
.L115:
	movl	$0,-64(%ebp)
	movl	$_u,%eax
	cmpl	$_s,%eax
	je	.L118
.L117:
	movl	$1,-84(%ebp)
	jmp	.L119
.L118:
	movl	$0,-84(%ebp)
.L119:
	movl	$_u,%eax
	cmpl	$_s+4,%eax
	je	.L121
.L120:
	movl	$1,-68(%ebp)
	jmp	.L122
.L121:
	movl	$0,-68(%ebp)
.L122:
	movl	$_u,%eax
	cmpl	$0,%eax
	je	.L124
.L123:
	movl	$1,-52(%ebp)
	jmp	.L125
.L124:
	movl	$0,-52(%ebp)
.L125:
	movl	$_u,%eax
	cmpl	$_u,%eax
	je	.L127
.L126:
	movl	$1,-76(%ebp)
	jmp	.L128
.L127:
	movl	$0,-76(%ebp)
.L128:
	movl	$_u,%eax
	cmpl	$_u,%eax
	je	.L130
.L129:
	movl	$1,-56(%ebp)
	jmp	.L131
.L130:
	movl	$0,-56(%ebp)
.L131:
	movl	$_u,%eax
	cmpl	$0,%eax
	je	.L133
.L132:
	movl	$1,-36(%ebp)
	jmp	.L134
.L133:
	movl	$0,-36(%ebp)
.L134:
	movl	$_u,%eax
	cmpl	$_u,%eax
	je	.L136
.L135:
	movl	$1,-60(%ebp)
	jmp	.L137
.L136:
	movl	$0,-60(%ebp)
.L137:
	movl	$_u,%eax
	cmpl	$_u,%eax
	je	.L139
.L138:
	movl	$1,-44(%ebp)
	jmp	.L140
.L139:
	movl	$0,-44(%ebp)
.L140:
	movl	$_u,%eax
	cmpl	$0,%eax
	je	.L142
.L141:
	movl	$1,-24(%ebp)
	jmp	.L143
.L142:
	movl	$0,-24(%ebp)
.L143:
	movl	$0,%eax
	cmpl	$_u,%eax
	je	.L145
.L144:
	movl	$1,-48(%ebp)
	jmp	.L146
.L145:
	movl	$0,-48(%ebp)
.L146:
	movl	$0,%eax
	cmpl	$_u,%eax
	je	.L148
.L147:
	movl	$1,-32(%ebp)
	jmp	.L151
.L148:
	movl	$0,-32(%ebp)
.L151:
	movl	$0,-16(%ebp)
	movl	$_a+8,%eax
	cmpl	$_a+8,%eax
	je	.L154
.L153:
	movl	$1,-40(%ebp)
	jmp	.L155
.L154:
	movl	$0,-40(%ebp)
.L155:
	movl	$_a+8,%eax
	cmpl	$_a+16,%eax
	je	.L157
.L156:
	movl	$1,-20(%ebp)
	jmp	.L158
.L157:
	movl	$0,-20(%ebp)
.L158:
	movl	$_a+8,%eax
	cmpl	$0,%eax
	je	.L160
.L159:
	movl	$1,-4(%ebp)
	jmp	.L161
.L160:
	movl	$0,-4(%ebp)
.L161:
	movl	$_a+16,%eax
	cmpl	$_a+8,%eax
	je	.L163
.L162:
	movl	$1,-28(%ebp)
	jmp	.L164
.L163:
	movl	$0,-28(%ebp)
.L164:
	movl	$_a+16,%eax
	cmpl	$_a+16,%eax
	je	.L166
.L165:
	movl	$1,-8(%ebp)
	jmp	.L167
.L166:
	movl	$0,-8(%ebp)
.L167:
	movl	$_a+16,%eax
	cmpl	$0,%eax
	je	.L169
.L168:
	movl	$1,%esi
	jmp	.L170
.L169:
	movl	$0,%esi
.L170:
	movl	$0,%eax
	cmpl	$_a+8,%eax
	je	.L172
.L171:
	movl	$1,-12(%ebp)
	jmp	.L173
.L172:
	movl	$0,-12(%ebp)
.L173:
	movl	$0,%eax
	cmpl	$_a+16,%eax
	je	.L175
.L174:
	movl	$1,%edi
	jmp	.L178
.L175:
	movl	$0,%edi
.L178:
	movl	$0,%ebx
	pushl	%ecx
	pushl	%edx
	pushl	-96(%ebp)
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
	pushl	-72(%ebp)
	pushl	-88(%ebp)
	pushl	-100(%ebp)
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
	pushl	-64(%ebp)
	pushl	-80(%ebp)
	pushl	-92(%ebp)
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
	pushl	-52(%ebp)
	pushl	-68(%ebp)
	pushl	-84(%ebp)
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
	pushl	-36(%ebp)
	pushl	-56(%ebp)
	pushl	-76(%ebp)
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
	pushl	-24(%ebp)
	pushl	-44(%ebp)
	pushl	-60(%ebp)
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
	pushl	-16(%ebp)
	pushl	-32(%ebp)
	pushl	-48(%ebp)
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
	pushl	-4(%ebp)
	pushl	-20(%ebp)
	pushl	-40(%ebp)
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
	pushl	%esi
	pushl	-8(%ebp)
	pushl	-28(%ebp)
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
	pushl	%ebx
	pushl	%edi
	pushl	-12(%ebp)
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
.L180:
	popl	%edi
	popl	%esi
	popl	%ebx
	leave
	ret


	.align	4
	.global	_main0l
_main0l:
	pushl	%ebp
	movl	%esp,%ebp
	subl	$132,%esp
	pushl	%ebx
	pushl	%esi
	pushl	%edi
	movl	$0,-28(%ebp)
	movl	$10,-24(%ebp)
	movl	$20,-20(%ebp)
	movl	$30,-16(%ebp)
	leal	-8(%ebp),%ecx
	leal	-8(%ebp),%eax
	cmpl	%eax,%ecx
	je	.L184
.L183:
	movl	$1,-132(%ebp)
	jmp	.L185
.L184:
	movl	$0,-132(%ebp)
.L185:
	leal	-8(%ebp),%ecx
	leal	-4(%ebp),%eax
	cmpl	%eax,%ecx
	je	.L187
.L186:
	movl	$1,-124(%ebp)
	jmp	.L188
.L187:
	movl	$0,-124(%ebp)
.L188:
	leal	-8(%ebp),%eax
	cmpl	$0,%eax
	je	.L190
.L189:
	movl	$1,%ecx
	jmp	.L191
.L190:
	movl	$0,%ecx
.L191:
	leal	-4(%ebp),%edx
	leal	-8(%ebp),%eax
	cmpl	%eax,%edx
	je	.L193
.L192:
	movl	$1,-128(%ebp)
	jmp	.L194
.L193:
	movl	$0,-128(%ebp)
.L194:
	leal	-4(%ebp),%edx
	leal	-4(%ebp),%eax
	cmpl	%eax,%edx
	je	.L196
.L195:
	movl	$1,-116(%ebp)
	jmp	.L197
.L196:
	movl	$0,-116(%ebp)
.L197:
	leal	-4(%ebp),%eax
	cmpl	$0,%eax
	je	.L199
.L198:
	movl	$1,-104(%ebp)
	jmp	.L200
.L199:
	movl	$0,-104(%ebp)
.L200:
	leal	-8(%ebp),%eax
	cmpl	$0,%eax
	je	.L202
.L201:
	movl	$1,-120(%ebp)
	jmp	.L203
.L202:
	movl	$0,-120(%ebp)
.L203:
	leal	-4(%ebp),%eax
	cmpl	$0,%eax
	je	.L205
.L204:
	movl	$1,-108(%ebp)
	jmp	.L208
.L205:
	movl	$0,-108(%ebp)
.L208:
	movl	$0,-100(%ebp)
	leal	-12(%ebp),%edx
	leal	-8(%ebp),%eax
	cmpl	%eax,%edx
	je	.L211
.L210:
	movl	$1,-112(%ebp)
	jmp	.L212
.L211:
	movl	$0,-112(%ebp)
.L212:
	leal	-12(%ebp),%edx
	leal	-4(%ebp),%eax
	cmpl	%eax,%edx
	je	.L214
.L213:
	movl	$1,-96(%ebp)
	jmp	.L215
.L214:
	movl	$0,-96(%ebp)
.L215:
	leal	-12(%ebp),%eax
	cmpl	$0,%eax
	je	.L217
.L216:
	movl	$1,-88(%ebp)
	jmp	.L218
.L217:
	movl	$0,-88(%ebp)
.L218:
	leal	-12(%ebp),%edx
	leal	-12(%ebp),%eax
	cmpl	%eax,%edx
	je	.L220
.L219:
	movl	$1,-92(%ebp)
	jmp	.L221
.L220:
	movl	$0,-92(%ebp)
.L221:
	leal	-12(%ebp),%edx
	leal	-12(%ebp),%eax
	cmpl	%eax,%edx
	je	.L223
.L222:
	movl	$1,-84(%ebp)
	jmp	.L224
.L223:
	movl	$0,-84(%ebp)
.L224:
	leal	-12(%ebp),%eax
	cmpl	$0,%eax
	je	.L226
.L225:
	movl	$1,-76(%ebp)
	jmp	.L227
.L226:
	movl	$0,-76(%ebp)
.L227:
	leal	-12(%ebp),%edx
	leal	-12(%ebp),%eax
	cmpl	%eax,%edx
	je	.L229
.L228:
	movl	$1,-80(%ebp)
	jmp	.L230
.L229:
	movl	$0,-80(%ebp)
.L230:
	leal	-12(%ebp),%edx
	leal	-12(%ebp),%eax
	cmpl	%eax,%edx
	je	.L232
.L231:
	movl	$1,-68(%ebp)
	jmp	.L233
.L232:
	movl	$0,-68(%ebp)
.L233:
	leal	-12(%ebp),%eax
	cmpl	$0,%eax
	je	.L235
.L234:
	movl	$1,-56(%ebp)
	jmp	.L236
.L235:
	movl	$0,-56(%ebp)
.L236:
	leal	-12(%ebp),%eax
	cmpl	$0,%eax
	je	.L238
.L237:
	movl	$1,-72(%ebp)
	jmp	.L239
.L238:
	movl	$0,-72(%ebp)
.L239:
	leal	-12(%ebp),%eax
	cmpl	$0,%eax
	je	.L241
.L240:
	movl	$1,-60(%ebp)
	jmp	.L244
.L241:
	movl	$0,-60(%ebp)
.L244:
	movl	$0,-52(%ebp)
	leal	-20(%ebp),%edx
	leal	-20(%ebp),%eax
	cmpl	%eax,%edx
	je	.L247
.L246:
	movl	$1,-64(%ebp)
	jmp	.L248
.L247:
	movl	$0,-64(%ebp)
.L248:
	leal	-20(%ebp),%edx
	leal	-12(%ebp),%eax
	cmpl	%eax,%edx
	je	.L250
.L249:
	movl	$1,-48(%ebp)
	jmp	.L251
.L250:
	movl	$0,-48(%ebp)
.L251:
	leal	-20(%ebp),%eax
	cmpl	$0,%eax
	je	.L253
.L252:
	movl	$1,-40(%ebp)
	jmp	.L254
.L253:
	movl	$0,-40(%ebp)
.L254:
	leal	-12(%ebp),%edx
	leal	-20(%ebp),%eax
	cmpl	%eax,%edx
	je	.L256
.L255:
	movl	$1,-44(%ebp)
	jmp	.L257
.L256:
	movl	$0,-44(%ebp)
.L257:
	leal	-12(%ebp),%edx
	leal	-12(%ebp),%eax
	cmpl	%eax,%edx
	je	.L259
.L258:
	movl	$1,-32(%ebp)
	jmp	.L260
.L259:
	movl	$0,-32(%ebp)
.L260:
	leal	-12(%ebp),%eax
	cmpl	$0,%eax
	je	.L262
.L261:
	movl	$1,%ebx
	jmp	.L263
.L262:
	movl	$0,%ebx
.L263:
	leal	-20(%ebp),%eax
	cmpl	$0,%eax
	je	.L265
.L264:
	movl	$1,-36(%ebp)
	jmp	.L266
.L265:
	movl	$0,-36(%ebp)
.L266:
	leal	-12(%ebp),%eax
	cmpl	$0,%eax
	je	.L268
.L267:
	movl	$1,%edi
	jmp	.L271
.L268:
	movl	$0,%edi
.L271:
	movl	$0,%esi
	pushl	%ecx
	pushl	-124(%ebp)
	pushl	-132(%ebp)
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
	pushl	-104(%ebp)
	pushl	-116(%ebp)
	pushl	-128(%ebp)
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
	pushl	-100(%ebp)
	pushl	-108(%ebp)
	pushl	-120(%ebp)
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
	pushl	-88(%ebp)
	pushl	-96(%ebp)
	pushl	-112(%ebp)
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
	pushl	-76(%ebp)
	pushl	-84(%ebp)
	pushl	-92(%ebp)
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
	pushl	-56(%ebp)
	pushl	-68(%ebp)
	pushl	-80(%ebp)
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
	pushl	-52(%ebp)
	pushl	-60(%ebp)
	pushl	-72(%ebp)
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
	pushl	-40(%ebp)
	pushl	-48(%ebp)
	pushl	-64(%ebp)
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
	pushl	%ebx
	pushl	-32(%ebp)
	pushl	-44(%ebp)
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
	pushl	%esi
	pushl	%edi
	pushl	-36(%ebp)
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
.L273:
	popl	%edi
	popl	%esi
	popl	%ebx
	leave
	ret


	.align	4
	.global	_main1
_main1:
	pushl	%ebp
	movl	%esp,%ebp
	pushl	%ebx
	movl	$_s,%ebx
	cmpl	$_s,%ebx
	je	.L277
.L276:
	movl	$1,%edx
	jmp	.L278
.L277:
	movl	$0,%edx
.L278:
	cmpl	$_s+4,%ebx
	je	.L280
.L279:
	movl	$1,%ecx
	jmp	.L281
.L280:
	movl	$0,%ecx
.L281:
	cmpl	$0,%ebx
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
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
	cmpl	$_s,%ebx
	je	.L286
.L285:
	movl	$1,%edx
	jmp	.L287
.L286:
	movl	$0,%edx
.L287:
	cmpl	$_s+4,%ebx
	je	.L289
.L288:
	movl	$1,%ecx
	jmp	.L290
.L289:
	movl	$0,%ecx
.L290:
	cmpl	$0,%ebx
	je	.L292
.L291:
	movl	$1,%eax
	jmp	.L293
.L292:
	movl	$0,%eax
.L293:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
	movl	$_s+4,%ebx
	cmpl	$_s,%ebx
	je	.L295
.L294:
	movl	$1,%edx
	jmp	.L296
.L295:
	movl	$0,%edx
.L296:
	cmpl	$_s+4,%ebx
	je	.L298
.L297:
	movl	$1,%ecx
	jmp	.L299
.L298:
	movl	$0,%ecx
.L299:
	cmpl	$0,%ebx
	je	.L301
.L300:
	movl	$1,%eax
	jmp	.L302
.L301:
	movl	$0,%eax
.L302:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
	cmpl	$_s,%ebx
	je	.L304
.L303:
	movl	$1,%edx
	jmp	.L305
.L304:
	movl	$0,%edx
.L305:
	cmpl	$_s+4,%ebx
	je	.L307
.L306:
	movl	$1,%ecx
	jmp	.L308
.L307:
	movl	$0,%ecx
.L308:
	cmpl	$0,%ebx
	je	.L310
.L309:
	movl	$1,%eax
	jmp	.L311
.L310:
	movl	$0,%eax
.L311:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
	movl	$0,%ebx
	movl	$0,%eax
	cmpl	$_s,%eax
	je	.L313
.L312:
	movl	$1,%edx
	jmp	.L314
.L313:
	movl	$0,%edx
.L314:
	cmpl	$_s+4,%ebx
	je	.L316
.L315:
	movl	$1,%ecx
	jmp	.L317
.L316:
	movl	$0,%ecx
.L317:
	cmpl	$0,%ebx
	je	.L319
.L318:
	movl	$1,%eax
	jmp	.L320
.L319:
	movl	$0,%eax
.L320:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
	cmpl	$_s,%ebx
	je	.L322
.L321:
	movl	$1,%edx
	jmp	.L323
.L322:
	movl	$0,%edx
.L323:
	cmpl	$_s+4,%ebx
	je	.L325
.L324:
	movl	$1,%ecx
	jmp	.L326
.L325:
	movl	$0,%ecx
.L326:
	cmpl	$0,%ebx
	je	.L328
.L327:
	movl	$1,%eax
	jmp	.L329
.L328:
	movl	$0,%eax
.L329:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
	movl	$_u,%ebx
	cmpl	$_s,%ebx
	je	.L331
.L330:
	movl	$1,%edx
	jmp	.L332
.L331:
	movl	$0,%edx
.L332:
	cmpl	$_s+4,%ebx
	je	.L334
.L333:
	movl	$1,%ecx
	jmp	.L335
.L334:
	movl	$0,%ecx
.L335:
	cmpl	$0,%ebx
	je	.L337
.L336:
	movl	$1,%eax
	jmp	.L338
.L337:
	movl	$0,%eax
.L338:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
	cmpl	$_s,%ebx
	je	.L340
.L339:
	movl	$1,%edx
	jmp	.L341
.L340:
	movl	$0,%edx
.L341:
	cmpl	$_s+4,%ebx
	je	.L343
.L342:
	movl	$1,%ecx
	jmp	.L344
.L343:
	movl	$0,%ecx
.L344:
	cmpl	$0,%ebx
	je	.L346
.L345:
	movl	$1,%eax
	jmp	.L347
.L346:
	movl	$0,%eax
.L347:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
.L348:
	popl	%ebx
	leave
	ret


	.align	4
	.global	_main2
_main2:
	pushl	%ebp
	movl	%esp,%ebp
	pushl	%ebx
	movl	$_u,%ebx
	cmpl	$_u,%ebx
	je	.L352
.L351:
	movl	$1,%edx
	jmp	.L353
.L352:
	movl	$0,%edx
.L353:
	cmpl	$_u,%ebx
	je	.L355
.L354:
	movl	$1,%ecx
	jmp	.L356
.L355:
	movl	$0,%ecx
.L356:
	cmpl	$0,%ebx
	je	.L358
.L357:
	movl	$1,%eax
	jmp	.L359
.L358:
	movl	$0,%eax
.L359:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
	cmpl	$_u,%ebx
	je	.L361
.L360:
	movl	$1,%edx
	jmp	.L362
.L361:
	movl	$0,%edx
.L362:
	cmpl	$_u,%ebx
	je	.L364
.L363:
	movl	$1,%ecx
	jmp	.L365
.L364:
	movl	$0,%ecx
.L365:
	cmpl	$0,%ebx
	je	.L367
.L366:
	movl	$1,%eax
	jmp	.L368
.L367:
	movl	$0,%eax
.L368:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
	movl	$_u,%ebx
	cmpl	$_u,%ebx
	je	.L370
.L369:
	movl	$1,%edx
	jmp	.L371
.L370:
	movl	$0,%edx
.L371:
	cmpl	$_u,%ebx
	je	.L373
.L372:
	movl	$1,%ecx
	jmp	.L374
.L373:
	movl	$0,%ecx
.L374:
	cmpl	$0,%ebx
	je	.L376
.L375:
	movl	$1,%eax
	jmp	.L377
.L376:
	movl	$0,%eax
.L377:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
	cmpl	$_u,%ebx
	je	.L379
.L378:
	movl	$1,%edx
	jmp	.L380
.L379:
	movl	$0,%edx
.L380:
	cmpl	$_u,%ebx
	je	.L382
.L381:
	movl	$1,%ecx
	jmp	.L383
.L382:
	movl	$0,%ecx
.L383:
	cmpl	$0,%ebx
	je	.L385
.L384:
	movl	$1,%eax
	jmp	.L386
.L385:
	movl	$0,%eax
.L386:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
	movl	$0,%ebx
	movl	$0,%eax
	cmpl	$_u,%eax
	je	.L388
.L387:
	movl	$1,%edx
	jmp	.L389
.L388:
	movl	$0,%edx
.L389:
	cmpl	$_u,%ebx
	je	.L391
.L390:
	movl	$1,%ecx
	jmp	.L392
.L391:
	movl	$0,%ecx
.L392:
	cmpl	$0,%ebx
	je	.L394
.L393:
	movl	$1,%eax
	jmp	.L395
.L394:
	movl	$0,%eax
.L395:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
	cmpl	$_u,%ebx
	je	.L397
.L396:
	movl	$1,%edx
	jmp	.L398
.L397:
	movl	$0,%edx
.L398:
	cmpl	$_u,%ebx
	je	.L400
.L399:
	movl	$1,%ecx
	jmp	.L401
.L400:
	movl	$0,%ecx
.L401:
	cmpl	$0,%ebx
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
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
.L405:
	popl	%ebx
	leave
	ret


	.align	4
	.global	_main3
_main3:
	pushl	%ebp
	movl	%esp,%ebp
	pushl	%ebx
	movl	$_a+8,%ebx
	cmpl	$_a+8,%ebx
	je	.L409
.L408:
	movl	$1,%edx
	jmp	.L410
.L409:
	movl	$0,%edx
.L410:
	cmpl	$_a+16,%ebx
	je	.L412
.L411:
	movl	$1,%ecx
	jmp	.L413
.L412:
	movl	$0,%ecx
.L413:
	cmpl	$0,%ebx
	je	.L415
.L414:
	movl	$1,%eax
	jmp	.L416
.L415:
	movl	$0,%eax
.L416:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
	cmpl	$_a+8,%ebx
	je	.L418
.L417:
	movl	$1,%edx
	jmp	.L419
.L418:
	movl	$0,%edx
.L419:
	cmpl	$_a+16,%ebx
	je	.L421
.L420:
	movl	$1,%ecx
	jmp	.L422
.L421:
	movl	$0,%ecx
.L422:
	cmpl	$0,%ebx
	je	.L424
.L423:
	movl	$1,%eax
	jmp	.L425
.L424:
	movl	$0,%eax
.L425:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
	movl	$_a+16,%ebx
	cmpl	$_a+8,%ebx
	je	.L427
.L426:
	movl	$1,%edx
	jmp	.L428
.L427:
	movl	$0,%edx
.L428:
	cmpl	$_a+16,%ebx
	je	.L430
.L429:
	movl	$1,%ecx
	jmp	.L431
.L430:
	movl	$0,%ecx
.L431:
	cmpl	$0,%ebx
	je	.L433
.L432:
	movl	$1,%eax
	jmp	.L434
.L433:
	movl	$0,%eax
.L434:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
	cmpl	$_a+8,%ebx
	je	.L436
.L435:
	movl	$1,%edx
	jmp	.L437
.L436:
	movl	$0,%edx
.L437:
	cmpl	$_a+16,%ebx
	je	.L439
.L438:
	movl	$1,%ecx
	jmp	.L440
.L439:
	movl	$0,%ecx
.L440:
	cmpl	$0,%ebx
	je	.L442
.L441:
	movl	$1,%eax
	jmp	.L443
.L442:
	movl	$0,%eax
.L443:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
	movl	$0,%ebx
	movl	$0,%eax
	cmpl	$_a+8,%eax
	je	.L445
.L444:
	movl	$1,%edx
	jmp	.L446
.L445:
	movl	$0,%edx
.L446:
	cmpl	$_a+16,%ebx
	je	.L448
.L447:
	movl	$1,%ecx
	jmp	.L449
.L448:
	movl	$0,%ecx
.L449:
	cmpl	$0,%ebx
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
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
	cmpl	$_a+8,%ebx
	je	.L454
.L453:
	movl	$1,%edx
	jmp	.L455
.L454:
	movl	$0,%edx
.L455:
	cmpl	$_a+16,%ebx
	je	.L457
.L456:
	movl	$1,%ecx
	jmp	.L458
.L457:
	movl	$0,%ecx
.L458:
	cmpl	$0,%ebx
	je	.L460
.L459:
	movl	$1,%eax
	jmp	.L461
.L460:
	movl	$0,%eax
.L461:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
.L462:
	popl	%ebx
	leave
	ret


	.align	4
	.global	_main4
_main4:
	pushl	%ebp
	movl	%esp,%ebp
	pushl	%ebx
	movl	$_s,%ebx
	cmpl	$_s,%ebx
	je	.L466
.L465:
	movl	$1,%edx
	jmp	.L467
.L466:
	movl	$0,%edx
.L467:
	cmpl	$_s+4,%ebx
	je	.L469
.L468:
	movl	$1,%ecx
	jmp	.L470
.L469:
	movl	$0,%ecx
.L470:
	cmpl	$0,%ebx
	je	.L472
.L471:
	movl	$1,%eax
	jmp	.L473
.L472:
	movl	$0,%eax
.L473:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
	cmpl	$_s,%ebx
	je	.L475
.L474:
	movl	$1,%edx
	jmp	.L476
.L475:
	movl	$0,%edx
.L476:
	cmpl	$_s+4,%ebx
	je	.L478
.L477:
	movl	$1,%ecx
	jmp	.L479
.L478:
	movl	$0,%ecx
.L479:
	cmpl	$0,%ebx
	je	.L481
.L480:
	movl	$1,%eax
	jmp	.L482
.L481:
	movl	$0,%eax
.L482:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
	movl	$_s+4,%ebx
	cmpl	$_s,%ebx
	je	.L484
.L483:
	movl	$1,%edx
	jmp	.L485
.L484:
	movl	$0,%edx
.L485:
	cmpl	$_s+4,%ebx
	je	.L487
.L486:
	movl	$1,%ecx
	jmp	.L488
.L487:
	movl	$0,%ecx
.L488:
	cmpl	$0,%ebx
	je	.L490
.L489:
	movl	$1,%eax
	jmp	.L491
.L490:
	movl	$0,%eax
.L491:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
	cmpl	$_s,%ebx
	je	.L493
.L492:
	movl	$1,%edx
	jmp	.L494
.L493:
	movl	$0,%edx
.L494:
	cmpl	$_s+4,%ebx
	je	.L496
.L495:
	movl	$1,%ecx
	jmp	.L497
.L496:
	movl	$0,%ecx
.L497:
	cmpl	$0,%ebx
	je	.L499
.L498:
	movl	$1,%eax
	jmp	.L500
.L499:
	movl	$0,%eax
.L500:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
	movl	$0,%ebx
	movl	$_s,%eax
	cmpl	$0,%eax
	je	.L502
.L501:
	movl	$1,%edx
	jmp	.L503
.L502:
	movl	$0,%edx
.L503:
	cmpl	$_s+4,%ebx
	je	.L505
.L504:
	movl	$1,%ecx
	jmp	.L506
.L505:
	movl	$0,%ecx
.L506:
	cmpl	$0,%ebx
	je	.L508
.L507:
	movl	$1,%eax
	jmp	.L509
.L508:
	movl	$0,%eax
.L509:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
	cmpl	$_s,%ebx
	je	.L511
.L510:
	movl	$1,%edx
	jmp	.L512
.L511:
	movl	$0,%edx
.L512:
	cmpl	$_s+4,%ebx
	je	.L514
.L513:
	movl	$1,%ecx
	jmp	.L515
.L514:
	movl	$0,%ecx
.L515:
	cmpl	$0,%ebx
	je	.L517
.L516:
	movl	$1,%eax
	jmp	.L518
.L517:
	movl	$0,%eax
.L518:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
	movl	$_u,%ebx
	cmpl	$_s,%ebx
	je	.L520
.L519:
	movl	$1,%edx
	jmp	.L521
.L520:
	movl	$0,%edx
.L521:
	cmpl	$_s+4,%ebx
	je	.L523
.L522:
	movl	$1,%ecx
	jmp	.L524
.L523:
	movl	$0,%ecx
.L524:
	cmpl	$0,%ebx
	je	.L526
.L525:
	movl	$1,%eax
	jmp	.L527
.L526:
	movl	$0,%eax
.L527:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
	cmpl	$_s,%ebx
	je	.L529
.L528:
	movl	$1,%edx
	jmp	.L530
.L529:
	movl	$0,%edx
.L530:
	cmpl	$_s+4,%ebx
	je	.L532
.L531:
	movl	$1,%ecx
	jmp	.L533
.L532:
	movl	$0,%ecx
.L533:
	cmpl	$0,%ebx
	je	.L535
.L534:
	movl	$1,%eax
	jmp	.L536
.L535:
	movl	$0,%eax
.L536:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
.L537:
	popl	%ebx
	leave
	ret


	.align	4
	.global	_main5
_main5:
	pushl	%ebp
	movl	%esp,%ebp
	pushl	%ebx
	movl	$_u,%ebx
	cmpl	$_u,%ebx
	je	.L541
.L540:
	movl	$1,%edx
	jmp	.L542
.L541:
	movl	$0,%edx
.L542:
	cmpl	$_u,%ebx
	je	.L544
.L543:
	movl	$1,%ecx
	jmp	.L545
.L544:
	movl	$0,%ecx
.L545:
	cmpl	$0,%ebx
	je	.L547
.L546:
	movl	$1,%eax
	jmp	.L548
.L547:
	movl	$0,%eax
.L548:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
	cmpl	$_u,%ebx
	je	.L550
.L549:
	movl	$1,%edx
	jmp	.L551
.L550:
	movl	$0,%edx
.L551:
	cmpl	$_u,%ebx
	je	.L553
.L552:
	movl	$1,%ecx
	jmp	.L554
.L553:
	movl	$0,%ecx
.L554:
	cmpl	$0,%ebx
	je	.L556
.L555:
	movl	$1,%eax
	jmp	.L557
.L556:
	movl	$0,%eax
.L557:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
	movl	$_u,%ebx
	cmpl	$_u,%ebx
	je	.L559
.L558:
	movl	$1,%edx
	jmp	.L560
.L559:
	movl	$0,%edx
.L560:
	cmpl	$_u,%ebx
	je	.L562
.L561:
	movl	$1,%ecx
	jmp	.L563
.L562:
	movl	$0,%ecx
.L563:
	cmpl	$0,%ebx
	je	.L565
.L564:
	movl	$1,%eax
	jmp	.L566
.L565:
	movl	$0,%eax
.L566:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
	cmpl	$_u,%ebx
	je	.L568
.L567:
	movl	$1,%edx
	jmp	.L569
.L568:
	movl	$0,%edx
.L569:
	cmpl	$_u,%ebx
	je	.L571
.L570:
	movl	$1,%ecx
	jmp	.L572
.L571:
	movl	$0,%ecx
.L572:
	cmpl	$0,%ebx
	je	.L574
.L573:
	movl	$1,%eax
	jmp	.L575
.L574:
	movl	$0,%eax
.L575:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
	movl	$0,%ebx
	movl	$_u,%eax
	cmpl	$0,%eax
	je	.L577
.L576:
	movl	$1,%edx
	jmp	.L578
.L577:
	movl	$0,%edx
.L578:
	cmpl	$_u,%ebx
	je	.L580
.L579:
	movl	$1,%ecx
	jmp	.L581
.L580:
	movl	$0,%ecx
.L581:
	cmpl	$0,%ebx
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
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
	cmpl	$_u,%ebx
	je	.L586
.L585:
	movl	$1,%edx
	jmp	.L587
.L586:
	movl	$0,%edx
.L587:
	cmpl	$_u,%ebx
	je	.L589
.L588:
	movl	$1,%ecx
	jmp	.L590
.L589:
	movl	$0,%ecx
.L590:
	cmpl	$0,%ebx
	je	.L592
.L591:
	movl	$1,%eax
	jmp	.L593
.L592:
	movl	$0,%eax
.L593:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
.L594:
	popl	%ebx
	leave
	ret


	.align	4
	.global	_main6
_main6:
	pushl	%ebp
	movl	%esp,%ebp
	pushl	%ebx
	movl	$_a+8,%ebx
	cmpl	$_a+8,%ebx
	je	.L598
.L597:
	movl	$1,%edx
	jmp	.L599
.L598:
	movl	$0,%edx
.L599:
	cmpl	$_a+16,%ebx
	je	.L601
.L600:
	movl	$1,%ecx
	jmp	.L602
.L601:
	movl	$0,%ecx
.L602:
	cmpl	$0,%ebx
	je	.L604
.L603:
	movl	$1,%eax
	jmp	.L605
.L604:
	movl	$0,%eax
.L605:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
	cmpl	$_a+8,%ebx
	je	.L607
.L606:
	movl	$1,%edx
	jmp	.L608
.L607:
	movl	$0,%edx
.L608:
	cmpl	$_a+16,%ebx
	je	.L610
.L609:
	movl	$1,%ecx
	jmp	.L611
.L610:
	movl	$0,%ecx
.L611:
	cmpl	$0,%ebx
	je	.L613
.L612:
	movl	$1,%eax
	jmp	.L614
.L613:
	movl	$0,%eax
.L614:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
	movl	$_a+16,%ebx
	cmpl	$_a+8,%ebx
	je	.L616
.L615:
	movl	$1,%edx
	jmp	.L617
.L616:
	movl	$0,%edx
.L617:
	cmpl	$_a+16,%ebx
	je	.L619
.L618:
	movl	$1,%ecx
	jmp	.L620
.L619:
	movl	$0,%ecx
.L620:
	cmpl	$0,%ebx
	je	.L622
.L621:
	movl	$1,%eax
	jmp	.L623
.L622:
	movl	$0,%eax
.L623:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
	cmpl	$_a+8,%ebx
	je	.L625
.L624:
	movl	$1,%edx
	jmp	.L626
.L625:
	movl	$0,%edx
.L626:
	cmpl	$_a+16,%ebx
	je	.L628
.L627:
	movl	$1,%ecx
	jmp	.L629
.L628:
	movl	$0,%ecx
.L629:
	cmpl	$0,%ebx
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
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
	movl	$0,%ebx
	movl	$_a+8,%eax
	cmpl	$0,%eax
	je	.L634
.L633:
	movl	$1,%edx
	jmp	.L635
.L634:
	movl	$0,%edx
.L635:
	cmpl	$_a+16,%ebx
	je	.L637
.L636:
	movl	$1,%ecx
	jmp	.L638
.L637:
	movl	$0,%ecx
.L638:
	cmpl	$0,%ebx
	je	.L640
.L639:
	movl	$1,%eax
	jmp	.L641
.L640:
	movl	$0,%eax
.L641:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
	cmpl	$_a+8,%ebx
	je	.L643
.L642:
	movl	$1,%edx
	jmp	.L644
.L643:
	movl	$0,%edx
.L644:
	cmpl	$_a+16,%ebx
	je	.L646
.L645:
	movl	$1,%ecx
	jmp	.L647
.L646:
	movl	$0,%ecx
.L647:
	cmpl	$0,%ebx
	je	.L649
.L648:
	movl	$1,%eax
	jmp	.L650
.L649:
	movl	$0,%eax
.L650:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
.L651:
	popl	%ebx
	leave
	ret


	.align	4
	.global	_main
_main:
	pushl	%ebp
	movl	%esp,%ebp
	call	_main0g
	call	_main0l
	call	_main1
	call	_main2
	call	_main3
	call	_main4
	call	_main5
	call	_main6
	pushl	$_s
	call	_f0
	leal	4(%esp),%esp
	pushl	$_s+4
	call	_f0
	leal	4(%esp),%esp
	pushl	$0
	call	_f0
	leal	4(%esp),%esp
	pushl	$_u
	call	_f0
	leal	4(%esp),%esp
	pushl	$_u
	call	_g0
	leal	4(%esp),%esp
	pushl	$_u
	call	_g0
	leal	4(%esp),%esp
	pushl	$0
	call	_g0
	leal	4(%esp),%esp
	pushl	$_a+8
	call	_h0
	leal	4(%esp),%esp
	pushl	$_a+16
	call	_h0
	leal	4(%esp),%esp
	pushl	$0
	call	_h0
	leal	4(%esp),%esp
	pushl	$_s
	call	_f1
	leal	4(%esp),%esp
	pushl	$_s+4
	call	_f1
	leal	4(%esp),%esp
	pushl	$0
	call	_f1
	leal	4(%esp),%esp
	pushl	$_u
	call	_f1
	leal	4(%esp),%esp
	pushl	$_u
	call	_g1
	leal	4(%esp),%esp
	pushl	$_u
	call	_g1
	leal	4(%esp),%esp
	pushl	$0
	call	_g1
	leal	4(%esp),%esp
	pushl	$_a+8
	call	_h1
	leal	4(%esp),%esp
	pushl	$_a+16
	call	_h1
	leal	4(%esp),%esp
	pushl	$0
	call	_h1
	leal	4(%esp),%esp
	pushl	$_s
	call	_f2
	leal	4(%esp),%esp
	pushl	$_s+4
	call	_f2
	leal	4(%esp),%esp
	pushl	$0
	call	_f2
	leal	4(%esp),%esp
	pushl	$_u
	call	_f2
	leal	4(%esp),%esp
	pushl	$_u
	call	_g2
	leal	4(%esp),%esp
	pushl	$_u
	call	_g2
	leal	4(%esp),%esp
	pushl	$0
	call	_g2
	leal	4(%esp),%esp
	pushl	$_a+8
	call	_h2
	leal	4(%esp),%esp
	pushl	$_a+16
	call	_h2
	leal	4(%esp),%esp
	pushl	$0
	call	_h2
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
	.align	4
	.global	_a
_a:
	.long	0
	.long	10
	.long	20
	.long	30
	.comm	_s,8
	.comm	_u,4
