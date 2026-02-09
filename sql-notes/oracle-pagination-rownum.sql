-- Oracle 11g/12c style pagination with ROWNUM
-- :offset = 0-based offset, :limit = page size

SELECT *
FROM (
  SELECT t.*, ROWNUM rn
  FROM (
    SELECT /*+ INDEX(t IDX_SOME_COL) */
           t.*
    FROM   SOME_TABLE t
    WHERE  t.STATUS = 'A'
    ORDER  BY t.CREATED_AT DESC
  ) t
  WHERE ROWNUM <= (:offset + :limit)
)
WHERE rn > :offset;
