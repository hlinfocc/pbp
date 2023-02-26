queryUserPermid
===
select 
	rp.* 
from pbp_admin_role ur 
left join pbp_role_permission rp on ur.roleid=rp.roleid 
left join pbp_permission pe on pe.id=rp.permid  
where ur.adminid='${userid}'

queryRolePermid
===
select * from pbp_role_permission where roleid='${roleid}'

queryUserPerm
===
select p.*, rp.has_btns from pbp_role_permission rp
left join pbp_permission p on p.id = rp.permid
where p.isdelete = 0 and rp.roleid in (
	select r.id from pbp_admin_role ar
	left join pbp_role r on r.id = ar.roleid 
	where r.isdelete = 0 and ar.adminid = '${adminid}'
)

queryUserRole
===
select r.* from pbp_admin_role ar
left join pbp_role r on r.id = ar.roleid
where r.isdelete = 0 and ar.adminid = '${adminid}'

queryLoginSuccessUrl
===
select route_path from pbp_role
where isdelete = 0 and id in (
	select roleid from pbp_admin_role
 	where adminid = '${ adminid }'
) 
order by pow desc
limit 1 offset 0

queryLoginSuccessroutePath
===
select route_path from pbp_role
where isdelete = 0 and id in ('${ roleid }') 
order by pow desc limit 1