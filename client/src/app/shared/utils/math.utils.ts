export const range = (
  start: number,
  stop: number,
  step: number = 1
): number[] => {
  if (step === 0) {
    throw new Error('Step cannot be zero.');
  }
  return Array.from(
    { length: Math.ceil((stop - start) / step) },
    (_, i) => start + i * step
  );
};
