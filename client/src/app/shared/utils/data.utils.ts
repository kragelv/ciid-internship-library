export const calcTotalPages = (
  total: number,
  limit: number | undefined,
  defaultLimit: number
) => {
  return Math.ceil(total / (limit || defaultLimit));
};

export interface AuthorInfo {
  firstName?: string;
  middleName?: string;
  lastName: string;
  birthYear?: number;
}

export const authorToString = ({
  firstName,
  middleName,
  lastName,
  birthYear,
}: AuthorInfo) => {
  const birthYearStr = birthYear ? `(${birthYear})` : '';
  return [lastName, firstName, middleName, birthYearStr]
    .filter(Boolean)
    .join(' ');
};
